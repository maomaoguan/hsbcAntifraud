package com.probro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.probro.exception.AntifraudException;
import com.probro.response.AntifraudResponse;
import com.probro.service.AntifraudService;
import com.probro.util.AntifraudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntifraudServiceImpl implements AntifraudService {

    @Override
    public AntifraudResponse process(JSONObject payload) throws AntifraudException {
        AntifraudResponse antifraudResponse = new AntifraudResponse();
        return antifraudResponse;
    }
}
