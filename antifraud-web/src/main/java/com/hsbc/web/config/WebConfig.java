package com.hsbc.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ResponseFilter responseFilter() {
        return new ResponseFilter();
    }

    @Bean
    public HandlerInterceptor frontTokenInterceptor() {
        return new FrontTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(frontTokenInterceptor()).
                addPathPatterns("/**").
                excludePathPatterns("/antifraud/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public FilterRegistrationBean filterRegister() {
        //注册过滤器
        FilterRegistrationBean registration = new FilterRegistrationBean(responseFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

}
