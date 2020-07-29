package com.imooc.mall.service.impl;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author shkstart
 * @create 2020-04-02 17:21
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * */

    @Override
    public ResponseVo<User> register(User user) {

        // 出现RuntimeException可以被拦截
        //error();

        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername > 0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail > 0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        user.setRole(RoleEnum.CUSTOMER.getCode());
        // MD5加密(Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //写入数据库
        int resultCount = userMapper.insertSelective(user);
        if(resultCount == 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 用户登录
     * */
    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUserName(username);
        if(user == null){
            // 用户不存在
            return ResponseVo.error(ResponseEnum.USERNAME_NOT_EXIST);
        }
        if(!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))){
            // 密码错误
            return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);
        }

        user.setPassword("");

        return ResponseVo.success(user);
    }

    public void error(){
        throw new RuntimeException("运行出错");
    }
}
