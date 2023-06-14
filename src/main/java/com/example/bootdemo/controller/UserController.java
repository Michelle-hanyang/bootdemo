package com.example.bootdemo.controller;

import com.example.bootdemo.pojo.entity.sys.User;
import com.example.bootdemo.service.sys.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: YANG
 * Date: 2023/4/24 16:38
 * Describe:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @GetMapping
    public String login(User user){
        return "Hello Spring Security";
    }

}
