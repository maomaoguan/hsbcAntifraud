package com.hsbc;


import com.alibaba.fastjson2.JSON;
import com.hsbc.constants.StatusEnum;
import com.hsbc.service.DataService;
import com.hsbc.service.RuleService;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.web"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class RuleTest {

    private static String VELOCITY_KEY = "amount";
    private static String RIGHT_KEY = "R_" + VELOCITY_KEY;
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private DataService dataService;


    @Test
    public void test1() {
        log.info("starting");

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

    private String genLine(Map<String, Object> featureParameters) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (Map.Entry<String, Object> featureParameter : featureParameters.entrySet()) {
            builder.append(featureParameter.getValue());

            if (i < featureParameters.size() - 1) {
                builder.append(',');
            }

            i++;
        }

        return builder.toString();
    }

    @Test
    public void testDataGen() throws Exception {
        Map<String, Object> featureParameters = new HashMap<>();

        featureParameters.put("fUserId", "43200601010002xxx");
        featureParameters.put("fMobile", "1332123");
        featureParameters.put("fAmount", 10);
        featureParameters.put("fEventTime", System.currentTimeMillis());
        featureParameters.put("fStatus", 0);

        for (int i = 0; i < 3; i++) {
            featureParameters.put("fUserId", "43200601010002xxx" + RandomUtils.nextInt(0, 3));

            List<String> lines = new ArrayList<>();

            for (int j = 0; j < 100; j++) {
                featureParameters.put("fAmount", RandomUtils.nextInt(0, 10));
                featureParameters.put("fEventTime", System.currentTimeMillis());

                String line = this.genLine(featureParameters);
                lines.add(line);
            }

            FileUtils.writeLines(new File("/Users/maomao/Documents/workspace/data/antifraud/" + i), lines);
        }
    }


}
