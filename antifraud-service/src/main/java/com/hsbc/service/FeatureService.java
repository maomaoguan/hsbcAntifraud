package com.hsbc.service;


import com.alibaba.fastjson.JSONObject;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;

import java.util.List;

/**
 * giving all functionalities regarding feature management and executions
 */
public interface FeatureService {
    public List<FeatureResultVo> execute(List<FeatureVo> features, JSONObject parameters);
}
