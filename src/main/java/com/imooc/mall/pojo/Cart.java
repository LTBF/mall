package com.imooc.mall.pojo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2020-04-04 18:24
 */
@Data
public class Cart {
    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
