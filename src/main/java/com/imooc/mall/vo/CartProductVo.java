package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2020-04-04 15:02
 */
@Data
public class CartProductVo {
    private Integer productId;

    // 购买数量
    private Integer quantity;

    private String productName;

    private String ProductSubtitle;

    private String ProductMainImage;

    private BigDecimal ProductPrice;

    private Integer ProductStatus;

    // 商品总价，数量*单价
    private BigDecimal ProductTotalPrice;

    private Integer ProductStock;

    // 商品是否选中
    private Boolean productSelected;


    public CartProductVo(Integer productId, Integer quantity, String productName, String productSubtitle, String productMainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        ProductSubtitle = productSubtitle;
        ProductMainImage = productMainImage;
        ProductPrice = productPrice;
        ProductStatus = productStatus;
        ProductTotalPrice = productTotalPrice;
        ProductStock = productStock;
        this.productSelected = productSelected;
    }
}
