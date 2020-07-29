package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shkstart
 * @create 2020-04-09 15:11
 */
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
