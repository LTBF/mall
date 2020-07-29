package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @author shkstart
 * @create 2020-04-05 18:15
 */
public interface IShippingService {

    /**
     * 添加收货地址
     * */
    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm shippingForm);

    /**
     * 删除收货地址
     * */
    ResponseVo delete(Integer uid, Integer shippingId);

    /**
     * 更新收货地址
     * */
    ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm);

    /**
     * 所有收货地址
     * */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

}
