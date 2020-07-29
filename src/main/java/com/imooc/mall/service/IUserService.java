package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;

/**
 * @author shkstart
 * @create 2020-04-02 17:18
 */
public interface IUserService {

    // 注册
    ResponseVo<User> register(User user);

    // 登录
    ResponseVo<User> login(String username, String password);

}
