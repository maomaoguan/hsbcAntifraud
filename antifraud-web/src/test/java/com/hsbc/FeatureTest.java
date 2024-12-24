package com.hsbc;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.service.DataService;
import com.hsbc.service.FeatureComputeService;
import com.hsbc.service.FeatureService;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Configuration
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class FeatureTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FeatureComputeService featureComputeService;

    @Test
    public void testData() throws Exception {
        dataService.init();

        List<Map<String, Object>> accountData = dataService.findDataByAccount("account1");

        log.info("testData {}", JSON.toJSONString(accountData));

        Assert.assertNotNull(accountData);
        Assert.assertTrue(accountData.size() > 0);
    }

    @Test
    public void testFeatureConf() throws Exception {
        featureService.init();

        Map<String, FeatureVo> features = featureService.getFeatures();

        log.info("features {}", JSON.toJSONString(features));

        Assert.assertNotNull(features);

        Assert.assertTrue(features.size() > 0);
    }

    @Test
    public void testFeatureCompute() throws Exception {
        dataService.init();
        featureService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account1");
        FeatureVo featureVo = featureService.getFeatures().get("xAmounts");

        FeatureResultVo resultVo = featureComputeService.compute(featureVo, parameters);

        log.info("resultVo {}", JSON.toJSONString(resultVo));

        Assert.assertNotNull(resultVo);
        Assert.assertTrue(resultVo.getStatus() == 0);
        Assert.assertTrue(resultVo.getValue() >= 0);
    }

    @Test
    public void testXAmounts() throws Exception {
        dataService.init();
        featureService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account3");
        FeatureVo featureVo = featureService.getFeatures().get("xAmounts");

        FeatureResultVo resultVo = featureComputeService.compute(featureVo, parameters);

        log.info("resultVo {}", JSON.toJSONString(resultVo));

        Assert.assertNotNull(resultVo);
        Assert.assertTrue(resultVo.getStatus() == 0);
        Assert.assertTrue(resultVo.getValue() > 430);
    }
}
