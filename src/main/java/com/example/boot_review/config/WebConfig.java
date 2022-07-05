package com.example.boot_review.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String connectPath = "/upload/**";
    private String resourcePath = "file:///D:/springboot_img/";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registr){
        registr.addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);
    }
}
