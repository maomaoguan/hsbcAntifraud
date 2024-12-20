package com.hsbc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import org.springframework.stereotype.Service;

@Service
public class AntifraudServiceImpl implements AntifraudService {

    @Override
    public AntifraudResponse process(JSONObject payload) throws AntifraudException {
        AntifraudResponse antifraudResponse = new AntifraudResponse();
        return antifraudResponse;
    }
}
