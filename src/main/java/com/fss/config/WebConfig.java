package com.fss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/user/ui").setViewName("/user_mgr");
        registry.addViewController("/user/changePwd").setViewName("/change_pwd");
        registry.addViewController("/file/upload").setViewName("/file_upload");
        registry.addViewController("/file/receive").setViewName("/file_receive");
        registry.addViewController("/file/showUploaded").setViewName("/show_uploaded");
        registry.addViewController("/file/showReceived").setViewName("/show_received");
        registry.addViewController("/catalog/ui").setViewName("/catalog_mgr");
        registry.addViewController("/client/ui").setViewName("/client_test");
        registry.addViewController("/client/button").setViewName("/client_button");
    }
}
