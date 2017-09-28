package com.fss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/user") public class UserController {
    @GetMapping("/ui") public String userUi() {
        return "view/user_mgr";
    }

    @GetMapping("/changePwd") public String changePwdUi() {
        return "view/change_pwd";
    }
}
