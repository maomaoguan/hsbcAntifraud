package com.hsbc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class}) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.web"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
@ServletComponentScan
public class HsbcFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(HsbcFrontApplication.class, args);
    }
}
