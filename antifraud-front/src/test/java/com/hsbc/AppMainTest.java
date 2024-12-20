package com.hsbc;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@EnableAsync //开启异步调用
@Configuration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
@PropertySource(value = {"classpath:app.properties"})
public class AppMainTest {

    private static String VELOCITY_KEY = "amount";
    private static String RIGHT_KEY = "R_" + VELOCITY_KEY;

    private static String TEST_DATA = "/Users/maomao/Documents/workspace/testdata/test1";

    private static List<String> EVENT_TYPES = new ArrayList<>(8);

    static {
        EVENT_TYPES.add("Login");
        EVENT_TYPES.add("Register");
        EVENT_TYPES.add("Trade");
    }

    @Test
    public void test1(){
        log.info("starting");
    }

}
