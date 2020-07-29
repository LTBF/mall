package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shkstart
 * @create 2020-04-06 15:03
 */
@Slf4j
@Transactional  // 测试类中添加，数据库回滚
public class IOrderServiceTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;

    private Integer uid = 1;

    private Integer shippingId = 4;

    private Long orderNo = Long.valueOf("1586167620468");

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void create() {
        ResponseVo<OrderVo> orderVoResponseVo = orderService.create(uid, shippingId);
        log.info("orderVoResponseVo={}", gson.toJson(orderVoResponseVo));
        Assert.assertEquals(orderVoResponseVo.getStatus(), ResponseEnum.SUCCESS.getCode());
    }

    @Test
    public void list(){
        ResponseVo<PageInfo> pageInfoResponseVo = orderService.list(uid, 1, 10);
        log.info("pageInfoResponseVo={}", gson.toJson(pageInfoResponseVo));
        Assert.assertEquals(pageInfoResponseVo.getStatus(), ResponseEnum.SUCCESS.getCode());
    }

    @Test
    public void detail(){
        ResponseVo<OrderVo> detail = orderService.detail(uid, orderNo);
        log.info("detail={}", gson.toJson(detail));
        Assert.assertEquals(detail.getStatus(), ResponseEnum.SUCCESS.getCode());
    }

    @Test
    public void cancle(){
        ResponseVo responseVo = orderService.cancle(uid, orderNo);
        log.info("cancle={}", gson.toJson(responseVo));
        Assert.assertEquals(responseVo.getStatus(), ResponseEnum.SUCCESS.getCode());
    }
}