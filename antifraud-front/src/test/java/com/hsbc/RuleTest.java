package com.hsbc;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.constants.StatusEnum;
import com.hsbc.service.DataService;
import com.hsbc.service.FeatureComputeService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.RuleService;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import com.hsbc.vo.RuleResultVo;
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

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FeatureComputeService featureComputeService;

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

    @Test
    public void testRule1() throws Exception {
        dataService.init();
        ruleService.init();
        featureService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account1");
        FeatureVo featureVo = featureService.getFeatures().get("xAmounts");
        FeatureResultVo resultVo = featureComputeService.compute(featureVo, parameters);

        List<FeatureResultVo> featureResults = new ArrayList<>();
        featureResults.add(resultVo);
        RuleResultVo ruleResultVo = ruleService.execute("scenario1", featureResults);

        log.info("testRule1 {}", JSON.toJSONString(ruleResultVo));

        Assert.assertNotNull(ruleResultVo);
        Assert.assertTrue(ruleResultVo.isHit());
    }

}
