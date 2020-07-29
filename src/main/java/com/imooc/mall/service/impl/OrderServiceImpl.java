package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.OrderStatus;
import com.imooc.mall.enums.PaymenType;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderItemVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-04-06 12:01
 */
@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    private Gson gson = new Gson();

    @Override
    @Transactional    // 事务控制, 默认RuntimeException时回滚
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        // 收货地址校验（总之要查出来）
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if(shipping == null){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        // 获取购物车中选中的商品
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        // 购物车没有选中的商品
        if(CollectionUtils.isEmpty(cartList)){
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }
        // 获取选中商品的productIds，根据productIds查出product信息,构造map(productId, product)
        Set<Integer> productIdSet = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream().collect(Collectors.toMap(Product::getId, product -> product));

        // 根据选中商品信息，构建订单的详情OrderItem列表
        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();    // 生成订单号
        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            if(product == null){    // 未找到该product
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, cart.getProductId().toString() + "商品不存在");
            }
            if(product.getStock() < cart.getQuantity()){    // 该product库存不足
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, product.getName() + "库存不足");
            }
            if(!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){    // 该product非在售状态
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, product.getName() + "商品下架或者删除");
            }

            // 构建OrderItem
            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            // 在此处削减库存，会不会导致数据库不一致（当删除库存后，但是却没有下单的话）？--->应该放到后面
            product.setStock(product.getStock() - cart.getQuantity());
            int rowForProduct = productMapper.updateByPrimaryKeySelective(product);
            if(rowForProduct == 0){
                ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        // 生成订单，入库 ，order和order_item-->事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);// 构建Order

        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder == 0){
            ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if(rowForOrderItem == 0){
            ResponseVo.error(ResponseEnum.ERROR);
        }

        // 更新购物车
        // Redis有事务（打包命令），不能回滚
        for(Cart cart : cartList){
            cartService.delete(uid, cart.getProductId());
        }

        // 构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo();

        List<Order> orderList = orderMapper.selectByUid(uid);
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        pageInfo.setList(orderVoList);;

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order  == null || order.getUserId() != uid){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancle(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order  == null || order.getUserId() != uid){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        // 只有未付款可以取消
        if(order.getStatus() != OrderStatus.NO_PAY.getCode()){
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatus.CANCLED.getCode());
        order.setCloseTime(new Date());
        int rows = orderMapper.updateByPrimaryKey(order);
        if(rows <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order  == null){
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        // 只有未付款可以变成已付款
        if(order.getStatus() != OrderStatus.NO_PAY.getCode()){
            throw  new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单id:" + orderNo);
        }
        order.setStatus(OrderStatus.PAYED.getCode());
        order.setPaymentTime(new Date());
        int rows = orderMapper.updateByPrimaryKey(order);
        if(rows <= 0){
            throw  new RuntimeException("将订单更新为已支付失败" + "订单id:" + orderNo);
        }

    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);

        if(shipping != null){
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId,
                            List<OrderItem> orderItemList
                            ) {
        Order order = new Order();

        order.setUserId(uid);
        order.setOrderNo(orderNo);
        order.setShippingId(shippingId);
        // stream 累加
        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setPayment(payment);
        order.setPaymentType(PaymenType.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatus.NO_PAY.getCode());

        return order;
    }

    /**
     * 企业级的话：分布式唯一ID
     * */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo,Integer quantity, Product product ) {
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        return orderItem;
    }


}
