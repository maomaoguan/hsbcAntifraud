package com.hsbc;


import com.alibaba.fastjson2.JSON;
import com.hsbc.constants.StatusEnum;
import com.hsbc.service.RuleService;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Configuration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.web"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
@PropertySource(value = {"classpath:app.properties"})
public class RuleTest {

    private static String VELOCITY_KEY = "amount";
    private static String RIGHT_KEY = "R_" + VELOCITY_KEY;
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RuleService ruleService;

    @Value("${antifraud.rules}")
    private String rules;

    @Test
    public void test1() {
        log.info("starting");

        log.info("rules {}", rules);
    }

    @Test
    public void test2() throws Exception {
        String content = fileUtil.loadFile("ruleconfig/rule1");

        log.info("test2 {} ", content);

        ruleService.init();

        RuleVo ruleVo = ruleService.findRuleByScenario("scenario1");

        log.info("test2 {}", JSON.toJSONString(ruleVo));

        Assert.assertNotNull(ruleVo);
        Assert.assertEquals(ruleVo.getStatus(), StatusEnum.ONLINE.getStatus());
    }

}
