package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shkstart
 * @create 2020-04-06 17:29
 */
@Getter
public enum PaymenType {

    PAY_ONLINE(1, "在线支付"),
    ;

    Integer code;
    String msg;

    PaymenType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
