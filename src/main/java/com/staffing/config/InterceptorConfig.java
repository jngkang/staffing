package com.staffing.config;

import com.staffing.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author JngKang
 * @date 2022-03-02 21:13
 * @description
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断token是否合法来决定是否需要登录
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/employee/login")
                .excludePathPatterns("/**/export")
                .excludePathPatterns("/**/exporttemp")
                .excludePathPatterns("/**/import")
                .excludePathPatterns("/files/**");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }


}