package com.hsbc;


import com.hsbc.util.AntifraudUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
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
@Slf4j
@Configuration
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class EnviTest {

    @Value("${antifraud.ruleNames}")
    private String rules;

    @Autowired
    private AntifraudUtil antifraudUtil;

    @Test
    public void test1() {
        log.info("starting");

        log.info("rules {}", rules);
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

                String line = antifraudUtil.genLine(featureParameters);
                lines.add(line);
            }

            FileUtils.writeLines(new File("/Users/maomao/Documents/workspace/data/antifraud/" + i), lines);
        }
    }

}
