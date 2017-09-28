package com.fss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/catalog") public class CatalogController {
    @GetMapping("/ui") public String catalogUi() {
        return "view/catalog_mgr";
    }
}
