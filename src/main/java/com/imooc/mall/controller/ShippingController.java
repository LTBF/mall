package com.imooc.mall.controller;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IShippingService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author shkstart
 * @create 2020-04-05 23:14
 */
@RestController
public class ShippingController {
    @Autowired
    IShippingService shippingService;

    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingForm shippingForm, HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        return shippingService.add(user.getId(), shippingForm);
    }

    @PostMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId, HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        return shippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm shippingForm,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        return shippingService.update(user.getId(),shippingId, shippingForm);
    }

    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        return shippingService.list(user.getId(),pageNum, pageSize);
    }
}
