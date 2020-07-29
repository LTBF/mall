package com.imooc.mall.controller;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.form.UserForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author shkstart
 * @create 2020-04-02 18:14
 */
@RestController  // @Controller + @ResponseBody
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;


    // 表单校验(@Valid), 校验结果BindingResult接收
    @PostMapping("/user/register")
    public ResponseVo register(@Valid @RequestBody UserForm userForm){
        User user = new User();
        BeanUtils.copyProperties(userForm, user); // 对象拷贝
        return userService.register(user);
    }

    // Session保存在内存里，改进版：token+redis
    @PostMapping("user/login")
    public ResponseVo login(HttpSession session,
                            @Valid @RequestBody UserForm userForm){
        ResponseVo responseVo = userService.login(userForm.getUsername(), userForm.getPassword());
        // 设置session
        session.setAttribute(MallConst.CURRENT_USER, responseVo.getData());

        return responseVo;
    }


    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}", session.getId());
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session) {
        log.info("/user/logout sessionId={}", session.getId());
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }

}
