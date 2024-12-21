package com.hsbc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsbc.service.FeatureService;
import com.hsbc.service.builder.FeatureBuilder;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FeatureServiceImpl implements FeatureService {
    private Map<String, FeatureVo> features = new HashMap();

    @Value("${antifraud.featureNames}")
    private String featureNames;

    @Autowired
    private FileUtil fileUtil;

    private final String featureConfigPrefix = "featureconfig/";

    @Autowired
    private FeatureBuilder featureBuilder;

    @Override
    public void init() {
        String[] featureTokens = StringUtils.split(featureNames, ",");
        for (String featureToken : featureTokens) {
            try {
                String rawContent = fileUtil.loadFile(featureConfigPrefix + StringUtils.trim(featureToken));
                FeatureVo featureVo = featureBuilder.buildFeature(rawContent);
                features.put(featureVo.getName(), featureVo);
            } catch (Exception ex) {
                log.error("[featureService] unable to init feature {}", featureToken, ex);
            }
        }
    }

    @Override
    public List<FeatureResultVo> execute(List<FeatureVo> features, JSONObject parameters) {
        return null;
    }

    public Map<String, FeatureVo> getFeatures() {
        return features;
    }
}
