package com.fss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/file") public class FileController {
    @GetMapping("/upload") public String fileUpload() {
        return "view/file_upload";
    }

    @GetMapping("/receive") public String fileReceive() {
        return "view/file_receive";
    }

    @GetMapping("/showUploaded") public String fileUploaded() {
        return "view/show_uploaded";
    }

    @GetMapping("/showReceived") public String fileReceived() {
        return "view/show_received";
    }
}
