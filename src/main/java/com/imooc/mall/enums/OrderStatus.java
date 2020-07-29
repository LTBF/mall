package com.imooc.mall.enums;

import lombok.Getter;

/**
 * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
 * @author shkstart
 * @create 2020-04-06 17:34
 */
@Getter
public enum OrderStatus {
    CANCLED(0, "已取消"),
    NO_PAY(10, "未付款"),
    PAYED(20, "已付款"),
    SHIPPED(40, "已发货"),
    TRADE_SUCESS(50, "交易成功"),
    TRADE_CLOSED(60, "交易关闭"),
    ;

    Integer code;
    String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
