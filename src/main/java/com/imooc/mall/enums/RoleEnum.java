package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shkstart
 * @create 2020-04-02 17:50
 */
@Getter
public enum  RoleEnum {
    ADMIN(0),

    CUSTOMER(1);

    Integer code;

    RoleEnum(Integer code){
        this.code = code;
    }
}
