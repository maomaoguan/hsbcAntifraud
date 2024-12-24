package com.hsbc;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.hsbc.util.MockUtil;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Configuration
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
@ComponentScan(basePackages = {"com.hsbc", "com.hsbc.front"})
@ImportResource(locations = {"classpath*:src/main/resources/spring.xml"})
public class MessagingTest {

    @Value("${antifraud.endpoint}")
    private String endpoint;

    @Value("${antifraud.queueName}")
    private String queueName;

    @Value("${antifraud.accessKey}")
    private String accessKey;

    @Value("${antifraud.accessId}")
    private String accessId;

    @Autowired
    private MockUtil mockUtil;

    private Message constructMessage(JSONObject payload) {
        Message message = new Message();
        message.setMessageBody(payload.toJSONString());
        return message;
    }

    @Test
    public void testSendingMsg() throws Exception {
        CloudAccount account = new CloudAccount(accessId, accessKey, endpoint);

        MNSClient mnsClient = account.getMNSClient();

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
        CloudAccount account = new CloudAccount(accessId, accessKey, endpoint);

        MNSClient mnsClient = account.getMNSClient();

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
