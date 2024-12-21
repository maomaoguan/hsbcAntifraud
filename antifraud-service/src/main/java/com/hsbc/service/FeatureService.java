package com.hsbc.service;


import com.alibaba.fastjson2.JSONObject;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;

import java.util.List;
import java.util.Map;

/**
 * giving all functionalities regarding feature management and executions
 */
public interface FeatureService {
    /**
     * feature engine initiations
     */
    public void init();

    public List<FeatureResultVo> execute(List<FeatureVo> features, JSONObject parameters);

    public Map<String, FeatureVo> getFeatures();
}
