package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.consts.MallConst;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.OrderCreateForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author shkstart
 * @create 2020-04-09 15:08
 */
@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(HttpSession session,
                                      @Valid @RequestBody OrderCreateForm orderCreateForm){

        User user = (User)session.getAttribute(MallConst.CURRENT_USER);
        ResponseVo<OrderVo> responseVo = orderService.create(user.getId(), orderCreateForm.getShippingId());

        return responseVo;
    }

    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(HttpSession session,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);
        ResponseVo<PageInfo> responseVo = orderService.list(user.getId(), pageNum, pageSize);

        return responseVo;
    }

    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(HttpSession session,
                                      @PathVariable Long orderNo){
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);
        ResponseVo<OrderVo> responseVo = orderService.detail(user.getId(), orderNo);

        return responseVo;
    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancle(HttpSession session,
                                      @PathVariable Long orderNo){
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);
        if(user == null)
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        ResponseVo responseVo = orderService.cancle(user.getId(), orderNo);

        return responseVo;
    }
}
