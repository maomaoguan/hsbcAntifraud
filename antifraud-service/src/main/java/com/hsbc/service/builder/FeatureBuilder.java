package com.hsbc.service.builder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.util.AntifraudUtil;
import com.hsbc.vo.FeatureVo;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeatureBuilder {
    @Autowired
    private AntifraudUtil antifraudUtil;

    public FeatureVo buildFeature(String rawContent) throws Exception {
        if (!antifraudUtil.isJSONValid(rawContent)) {
            throw new Exception("illegal feature json found");
        }

        JSONObject featureJson = JSON.parseObject(rawContent);

        FeatureVo featureVo = new FeatureVo();
        featureVo.setName(featureJson.getString("name"));
        featureVo.setStatus(featureJson.getString("status"));
        featureVo.setFuncType(featureJson.getString("funcType"));
        featureVo.setFieldName(featureJson.getString("fieldName"));
        featureVo.setFilter(featureJson.getString("filter"));

        return featureVo;
    }
}