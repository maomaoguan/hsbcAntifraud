package com.hsbc;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import com.hsbc.service.FeatureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@Slf4j
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.web"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class AntifraudTest {

    @Autowired
    private AntifraudService antifraudService;

    @Test
    public void testScenario3() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account2");
        parameters.put("scenarioId", "scenario3");
        parameters.put("fEventTime", System.currentTimeMillis());

        AntifraudResponse antifraudResponse = antifraudService.process(parameters);

        log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

        Assert.assertNotNull(antifraudResponse);
    }
}
