package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//把它定义成一个controller
@RestController
public class WebController {
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

//http://localhost:8080/admin?name=admin

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("admin".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("user".equals(account.getRole())) {
            dbAccount = userService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

}
