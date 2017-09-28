package com.fss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/client") public class ClientTestController {

    @GetMapping("/ui") public String addClientUi() {
        return "view/client_test";
    }

    @GetMapping("/button") public String addClientButton() {
        return "view/client_button";
    }

}
