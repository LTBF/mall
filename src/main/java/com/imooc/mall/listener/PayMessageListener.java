package com.imooc.mall.listener;

import com.google.gson.Gson;
import com.imooc.mall.pojo.PayInfo;
import com.imooc.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 关于PayInfo，正确姿势：pay提供clent.jar, mall引用jar包
 * @author shkstart
 * @create 2020-04-09 17:01
 */
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMessageListener {
    @Autowired
    private IOrderService orderService;

    @RabbitHandler
    public void process(String msg){
        log.info("接收消息={}", msg);

        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if(payInfo.getPlatformStatus().equals("SUCESS")){
            //修改订单状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
