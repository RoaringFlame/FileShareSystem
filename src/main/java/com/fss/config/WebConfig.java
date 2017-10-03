package com.fss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("view/index");
        registry.addViewController("/login").setViewName("view/login");
        registry.addViewController("/user/ui").setViewName("view/user_mgr");
        registry.addViewController("/user/changePwd").setViewName("view/change_pwd");
        registry.addViewController("/file/upload").setViewName("view/file_upload");
        registry.addViewController("/file/receive").setViewName("view/file_receive");
        registry.addViewController("/file/showUploaded").setViewName("view/show_uploaded");
        registry.addViewController("/file/showReceived").setViewName("view/show_received");
        registry.addViewController("/catalog/ui").setViewName("view/catalog_mgr");
        registry.addViewController("/client/ui").setViewName("view/client_test");
        registry.addViewController("/client/button").setViewName("view/client_button");
    }
}
