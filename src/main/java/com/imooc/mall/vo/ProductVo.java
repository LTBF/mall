package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2020-04-04 9:23
 */
@Data
public class ProductVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
