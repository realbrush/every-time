package com.example.everytime.config;

import com.example.everytime.interceptor.SessionCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new SessionCheckInterceptor())
                .addPathPatterns("**")
                .excludePathPatterns("/login","/register")
                .excludePathPatterns("/swagger-ui/**","swagger-resources/**","/v2/api-docs");
    }
}
