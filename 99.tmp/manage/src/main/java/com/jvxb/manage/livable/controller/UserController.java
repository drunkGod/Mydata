package com.jvxb.manage.livable.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登陆过系统的用户 前端控制器
 * </p>
 *
 * @author lichunlong
 * @since 2019-10-10
 */
@RestController
@Api(tags = "test")
public class UserController {

    @GetMapping ("test")
    @ApiOperation("测试接口")
    public Object test(String user) throws Exception {
        System.out.println("user :" + user);
        return user;
    }

}

