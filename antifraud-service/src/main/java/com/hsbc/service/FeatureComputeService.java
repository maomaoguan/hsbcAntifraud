package com.hsbc.service;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.exception.ComputeException;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;

import java.util.List;

public interface FeatureComputeService {
    public FeatureResultVo compute(FeatureVo featureVo, JSONObject parameters) throws ComputeException;

    /**
     * computes all features in a paralleled way
     *
     * @param featureVos
     * @param parameters
     * @return
     * @throws ComputeException
     */
    public List<FeatureResultVo> compute(List<FeatureVo> featureVos, JSONObject parameters) throws ComputeException;
}
