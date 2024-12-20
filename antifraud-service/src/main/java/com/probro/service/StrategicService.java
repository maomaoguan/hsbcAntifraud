package com.probro.service;

import com.alibaba.fastjson.JSONObject;
import com.probro.exception.AntifraudException;
import com.probro.response.StrategicResponse;

public interface StrategicService {
    public StrategicResponse process(JSONObject payload) throws AntifraudException;
}
