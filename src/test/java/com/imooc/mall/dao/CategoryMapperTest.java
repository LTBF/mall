package com.imooc.mall.dao;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.pojo.Category;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shkstart
 * @create 2020-03-23 10:59
 */
public class CategoryMapperTest extends MallApplicationTests {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void findById() {
        Category byId = categoryMapper.selectByPrimaryKey(100001);
        System.out.println(byId.toString());
    }

    @Test
    public void queryById() {
        Category byId = categoryMapper.selectByPrimaryKey(100001);
        System.out.println(byId.toString());
    }
}