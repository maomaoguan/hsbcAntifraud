package com.hsbc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsbc.service.FeatureService;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureServiceImpl implements FeatureService {
    @Override
    public List<FeatureResultVo> execute(List<FeatureVo> features, JSONObject parameters) {
        return null;
    }
}
