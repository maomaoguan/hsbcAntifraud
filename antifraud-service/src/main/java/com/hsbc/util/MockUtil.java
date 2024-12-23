package com.hsbc.util;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MockUtil {
    public JSONObject mockPayload1() {
        JSONObject payload = new JSONObject();

        payload.put("scenarioId", "scenario1");
        payload.put("fAccountId", "account1");
        payload.put("fEventTime", System.currentTimeMillis());

        return payload;
    }
}
