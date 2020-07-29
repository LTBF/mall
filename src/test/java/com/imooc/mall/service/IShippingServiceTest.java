package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author shkstart
 * @create 2020-04-05 18:39
 */
@Slf4j
public class IShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;


    @Test
    public void add() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("张三");
        shippingForm.setReceiverPhone("1");
        shippingForm.setReceiverAddress("s");
        shippingForm.setReceiverCity("s");
        shippingForm.setReceiverDistrict("s");
        shippingForm.setReceiverProvince("s");
        shippingForm.setReceiverMobile("9");

        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, shippingForm);
        log.info("responseVo={}", responseVo);
    }

    @Test
    public void delete() {
        ResponseVo responseVo = shippingService.delete(11, 6);
        log.info("responseVo={}", responseVo);
    }

    @Test
    public void update() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("张三");
        ResponseVo responseVo = shippingService.update(11, 6, shippingForm);
        log.info("responseVo={}", responseVo);
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> list = shippingService.list(11, 0, 10);
        log.info("responseVo={}", list);
    }
}