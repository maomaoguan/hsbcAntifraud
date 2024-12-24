package com.hsbc.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.hsbc.service.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqServiceImpl implements MqService {

    @Value("${antifraud.endpoint}")
    private String endpoint;

    @Value("${antifraud.queueName}")
    private String queueName;

    @Value("${antifraud.accessKey}")
    private String accessKey;

    @Value("${antifraud.accessId}")
    private String accessId;

    public String getQueueName() {
        return queueName;
    }

    @Override
    public MNSClient acquireMqClient() {
        CloudAccount account = new CloudAccount(accessId, accessKey, endpoint);
        MNSClient mnsClient = account.getMNSClient();
        return mnsClient;
    }

    public Message constructMessage(JSONObject payload) {
        Message message = new Message();
        message.setMessageBody(payload.toJSONString());
        return message;
    }

}
