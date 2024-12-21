package com.hsbc.service;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;

public interface AntifraudService {
    public AntifraudResponse process(JSONObject payload) throws AntifraudException;
}
