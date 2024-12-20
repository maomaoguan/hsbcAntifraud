package com.probro.service;

import com.alibaba.fastjson.JSONObject;
import com.probro.exception.AntifraudException;
import com.probro.response.AntifraudResponse;

public interface AntifraudService {
    public AntifraudResponse process(JSONObject payload) throws AntifraudException;
}
