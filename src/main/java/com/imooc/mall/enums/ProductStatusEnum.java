package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shkstart
 * @create 2020-04-04 18:07
 */
@Getter
public enum ProductStatusEnum {
    ON_SALE(1),
    OFF_SALE(2),
    DELETE(3),
    ;

    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
