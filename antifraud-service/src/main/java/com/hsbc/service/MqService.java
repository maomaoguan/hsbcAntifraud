package com.hsbc.service;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;

public interface MqService {
    public String getQueueName();
    public MNSClient acquireMqClient();
    public Message constructMessage(JSONObject payload);
}
