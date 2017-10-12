package com.fss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAsync
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setKeepAliveSeconds(300);
        return executor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("view/index");
        registry.addViewController("/login").setViewName("view/login");
        registry.addViewController("/user/ui").setViewName("view/user_mgr");
        registry.addViewController("/view/changePwd").setViewName("view/change_pwd");
        registry.addViewController("/view/upload").setViewName("view/file_upload");
        registry.addViewController("/view/receive").setViewName("view/file_receive");
        registry.addViewController("/view/showUploaded").setViewName("view/show_uploaded");
        registry.addViewController("/view/showReceived").setViewName("view/show_received");
        registry.addViewController("/catalog/ui").setViewName("view/catalog_mgr");
        registry.addViewController("/client/ui").setViewName("view/client_test");
        registry.addViewController("/client/button").setViewName("view/client_button");
    }
}
