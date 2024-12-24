package com.hsbc;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.hsbc.service.MqService;
import com.hsbc.util.MockUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Configuration
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class MessagingTest {


    @Value("${antifraud.queueName}")
    private String queueName;

    @Autowired
    private MqService mqService;

    @Autowired
    private MockUtils mockUtil;


    private Message constructMessage(JSONObject payload) {
        Message message = new Message();
        message.setMessageBody(payload.toJSONString());
        return message;
    }

    @Test
    public void testSendingMsg() throws Exception {
        MNSClient mnsClient = mqService.acquireMqClient();

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);

            for (int i = 0; i < 100; i++) {
                JSONObject payload = mockUtil.mockPayload1();

                cloudQueue.putMessage(constructMessage(payload));
            }
        } catch (Exception ex) {
            log.error("[testSendingMsg] failed to send to endpoint ", ex);
        }

        mnsClient.close();
    }


    //    @Test
    public void testReceivingMsg() throws Exception {
        MNSClient mnsClient = mqService.acquireMqClient();

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);

            for (int i = 0; i < 10; i++) {
                Message message = cloudQueue.popMessage(2);
                if (message == null) {
                    log.info("[testReceivingMsg] no messages");
                    break;
                }

                log.info("[testReceivingMsg] messageReceived-{} {}", i, message.getMessageBody());
            }
        } catch (Exception ex) {
            log.error("[testReceivingMsg] failed to receive ", ex);
        }

        mnsClient.close();
    }
}
