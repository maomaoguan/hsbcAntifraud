package com.hsbc;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.hsbc.constants.CodeEnum;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.MqService;
import com.hsbc.util.MockUtils;
import com.hsbc.web.controller.MessagingController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.AbstractListDecorator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * integration tests against the whole anti-fraud flow
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@Slf4j
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.web"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class AntifraudTest {

    @Autowired
    private AntifraudService antifraudService;

    @Autowired
    private MockUtils mockUtils;
    @Autowired
    private MessagingController messagingController;
    @Autowired
    private MqService mqService;

    /**
     * integration test in an invocation way, detections will be printed out in logs
     *
     * @throws Exception
     */
    @Test
    public void testIntegration1() throws Exception {
        antifraudService.init();
//        messagingController.start(mockUtils.mockHttpRequest());

        List<JSONObject> payloads = mockUtils.mockRealPayload();

        for (JSONObject payload : payloads) {
            AntifraudResponse antifraudResponse = this.antifraudService.process(payload);

            if (CodeEnum.typeOf(antifraudResponse.getCode()) == CodeEnum.REJECTED) {
                log.info("[testIntegration1] fraud detectioned - {}  {}", antifraudResponse.getAccountId(), antifraudResponse.getDetails());
            }
        }

        log.info("[testIntegration1] completed");
    }


    /**
     * integration test in a messaging way, detections will be printed out in logs
     *
     * @throws Exception
     */
    @Test
    public void testIntegration2() throws Exception {
        antifraudService.init();
        /**
         * this node will consume messages if following opens
         */
//        messagingController.start(mockUtils.mockHttpRequest());

        List<JSONObject> payloads = mockUtils.mockRealPayload();

        MNSClient mnsClient = mqService.acquireMqClient();
        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(mqService.getQueueName());

            for (int i = 0; i < payloads.size(); i++) {
                cloudQueue.putMessage(mqService.constructMessage(payloads.get(i)));
            }

            mnsClient.close();

            log.info("[testIntegration2] {} messages sent ", payloads.size());
        } catch (Exception ex) {
            log.error("[testSendingMsg] failed to send to endpoint ", ex);
        }

        /**
         * suspend waiting for messages to get processed
         */
        Thread.sleep(1000 * 20);


        log.info("[testIntegration2] completed");
    }

    /**
     * scenario test against certain scenario like rule1, rule2
     *
     * @throws Exception
     */
    @Test
    public void testScenario3() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account5");
        parameters.put("scenarioId", "scenario3");
        parameters.put("fEventTime", System.currentTimeMillis());

        AntifraudResponse antifraudResponse = antifraudService.process(parameters);

        log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

        Assert.assertNotNull(antifraudResponse);
    }


    @Test
    public void testScenario2() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account3");
        parameters.put("scenarioId", "scenario2");
        parameters.put("fEventTime", System.currentTimeMillis());

        AntifraudResponse antifraudResponse = antifraudService.process(parameters);

        log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

        Assert.assertNotNull(antifraudResponse);
        Assert.assertEquals(CodeEnum.REJECTED.getCode(), antifraudResponse.getCode());
    }


    @Test
    public void testScenario1() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account1");
        parameters.put("scenarioId", "scenario1");
        parameters.put("fEventTime", System.currentTimeMillis());

        AntifraudResponse antifraudResponse = antifraudService.process(parameters);

        log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

        Assert.assertNotNull(antifraudResponse);
        Assert.assertEquals(CodeEnum.PASSED.getCode(), antifraudResponse.getCode());
    }


    @Test
    public void testDetectedScenario1() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account3");
        parameters.put("scenarioId", "scenario2");
        parameters.put("fEventTime", System.currentTimeMillis());

        AntifraudResponse antifraudResponse = antifraudService.process(parameters);

        log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

        Assert.assertNotNull(antifraudResponse);
    }


    /**
     * assert throws with illegal args
     *
     * @throws Exception
     */
    @Test
    public void testAnomaly1() throws Exception {
        antifraudService.init();

        JSONObject parameters = new JSONObject();
        parameters.put("fAccountId", "account3");
//        parameters.put("scenarioId", "scenario2");
        parameters.put("fEventTime", System.currentTimeMillis());

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                AntifraudResponse antifraudResponse = antifraudService.process(parameters);

                log.info("antifraudResponse {}", JSON.toJSONString(antifraudResponse));

            }
        });
    }


}
