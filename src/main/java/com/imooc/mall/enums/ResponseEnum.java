package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shkstart
 * @create 2020-04-03 11:40
 */
@Getter
public enum ResponseEnum {
    ERROR(-1, "服务端错误"),
    SUCCESS(0, "成功"),
    PASSWORD_ERROR(1, "密码错误"),
    USERNAME_EXIST(2, "用户已存在"),
    PARAM_ERROR(3, "参数错误"),
    EMAIL_EXIST(4, "email已存在"),
    USERNAME_NOT_EXIST(3, "用户名不存在"),
    NEED_LOGIN(10, "需要登录"),
    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),
    PRODUCT_OFF_SALE_OR_DELETE(12, "商品下架或删除"),
    PRODUCT_NOT_EXIST(13, "商品不存在"),
    PRODUCT_STOCK_ERROR(14, "库存有误"),
    CART_PRODUCT_NOT_EXIST(15, "购物车无此商品"),
    SHIPPING_DELETE_ERROR(16, "收获地址删除失败"),
    SHIPPING_NOT_EXIST(17, "收获地址不存在"),
    CART_SELECTED_IS_EMPTY(18, "请选择商品后下单"),
    ORDER_NOT_EXIST(19, "商品不存在"),
    ORDER_STATUS_ERROR(20, "商品不存在"),
    ;

    Integer code;
    String  desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
