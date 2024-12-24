package com.hsbc.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.service.FeatureComputeService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.builder.FeatureBuilder;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private FeatureComputeService featureComputeService;

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
        List<FeatureResultVo> featureResultVos = new ArrayList<>();

        try {
            featureResultVos = featureComputeService.compute(features, parameters);
        } catch (Exception ex) {
            log.error("[featureService] failed compute features {}", JSON.toJSONString(features), ex);
            FeatureResultVo featureResultVo = new FeatureResultVo();
            featureResultVo.setStatus(CodeEnum.SYSTEM_ERROR.getCode());

            featureResultVos.add(featureResultVo);
        }

//
//        for (FeatureVo feature : features) {
//            try {
//                featureResultVos.add(featureComputeService.compute(feature, parameters));
//            } catch (Exception ex) {
//                log.error("[featureService] failed compute feature {}", feature.getName(), ex);
//                FeatureResultVo featureResultVo = new FeatureResultVo();
//                featureResultVo.setStatus(CodeEnum.SYSTEM_ERROR.getCode());
//
//                featureResultVos.add(featureResultVo);
//            }
//        }

        return featureResultVos;
    }

    @Override
    public Map<String, FeatureVo> getFeatures() {
        return features;
    }
}
