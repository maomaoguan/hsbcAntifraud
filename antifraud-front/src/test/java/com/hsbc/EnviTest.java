package com.hsbc;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
//@Configuration
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
//@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
//@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
//@PropertySource(value = {"classpath:app.properties"})
public class EnviTest {

    @Value("${antifraud.rules}")
    private String rules;

    @Test
    public void test1() {
        log.info("starting");

        log.info("rules {}", rules);
    }

}
