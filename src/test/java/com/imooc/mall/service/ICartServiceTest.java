package com.imooc.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shkstart
 * @create 2020-04-04 18:38
 */
@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    // 将字符串按json格式排列
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(26);
        cartAddForm.setSelected(true);

        cartService.add(1,cartAddForm);
    }

    @Test
    public void list(){
        ResponseVo<CartVo> responseVo = cartService.list(1);
        log.info("responseVo={}", gson.toJson(responseVo));
    }

    @Test
    public void update(){

        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(2);
        cartUpdateForm.setSelected(false);

        ResponseVo<CartVo> responseVo = cartService.update(1, 26,cartUpdateForm);
        log.info("responseVo={}", gson.toJson(responseVo));
    }

    @Test
    public void delete(){
        ResponseVo<CartVo> responseVo = cartService.delete(1, 26);
        log.info("responseVo={}", gson.toJson(responseVo));
    }

    @Test
    public void selectAll(){
        ResponseVo<CartVo> responseVo = cartService.selectAll(1);
        log.info("responseVo={}", gson.toJson(responseVo));
    }
    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(1);
        log.info("responseVo={}", gson.toJson(responseVo));
    }

    @Test
    public void sum(){
        ResponseVo<Integer> responseVo = cartService.sum(1);
        log.info("responseVo={}", gson.toJson(responseVo));
    }
}