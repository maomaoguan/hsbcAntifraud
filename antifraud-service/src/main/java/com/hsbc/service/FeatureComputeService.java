package com.hsbc.service;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.exception.ComputeException;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;

public interface FeatureComputeService {
    public FeatureResultVo compute(FeatureVo featureVo, JSONObject parameters) throws ComputeException;
}
