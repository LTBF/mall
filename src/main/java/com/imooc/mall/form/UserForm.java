package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shkstart
 * @create 2020-04-03 11:53
 */
@Data
public class UserForm {
    @NotBlank    //用于String，判断空格
    //@NotNull
    //@NotEmpty 用于集和，是否为空
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
