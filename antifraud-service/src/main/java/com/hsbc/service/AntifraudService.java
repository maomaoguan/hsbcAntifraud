package com.hsbc.service;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;

public interface AntifraudService {
    /**
     * initiation of all antifraud related resources, including rule, feature, data, etc
     */
    public void init();

    /**
     * the requested payload within, there is a few assumptions regarding payload sending
     * 1. scenarioId is mandatory, which identifies current scenario and also there is rule attached accordingly
     * 2. fAccountId is mandatory, which identifies the account that will be detected against
     * 3. other parameters will start with a f prefix,
     * which indicates it is a parameter that can be used in feature computes or rule computes, like fEventTime points to current executing time
     * @param payload
     * @return
     * @throws AntifraudException
     */
    public AntifraudResponse process(JSONObject payload) throws AntifraudException;
}
