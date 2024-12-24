package com.hsbc.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.service.AviatorService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.RuleService;
import com.hsbc.service.builder.RuleBuilder;
import com.hsbc.util.AntifraudUtil;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import com.hsbc.vo.RuleResultVo;
import com.hsbc.vo.RuleVo;
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
public class RuleServiceImpl implements RuleService {
    private HashMap<String, RuleVo> rules = new HashMap<>();

    @Value("${antifraud.ruleNames}")
    private String ruleNames;

    private final String ruleConfPrefix = "ruleconfig/";

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private AntifraudUtil antifraudUtil;

    @Autowired
    private RuleBuilder ruleBuilder;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private AviatorService aviatorService;

    @Override
    public void init() {
        String[] ruleTokens = StringUtils.split(ruleNames, ",");
        for (String ruleToken : ruleTokens) {
            try {
                String rawContent = fileUtil.loadFile(ruleConfPrefix + StringUtils.trim(ruleToken));
                RuleVo ruleVo = ruleBuilder.buildRule(rawContent);
                postRuleBuild(ruleVo);

                rules.put(ruleVo.getScenarioId(), ruleVo);
            } catch (Exception ex) {
                log.error("[ruleService] unable to init rule {}", ruleToken, ex);
            }
        }
    }

    /**
     * attaching dependent features to a rule
     *
     * @param ruleVo
     */
    private void postRuleBuild(RuleVo ruleVo) {
        if (StringUtils.isNotBlank(ruleVo.getRule())) {
            List<String> fieldsOrFeatures = aviatorService.findVariables(ruleVo.getRule());
            List<FeatureVo> features = new ArrayList<>();
            for (String fieldOrFeature : fieldsOrFeatures) {
                if (StringUtils.startsWith(fieldOrFeature, "x")) {
                    features.add(featureService.getFeatures().get(fieldOrFeature));
                }
            }
            ruleVo.setFeatures(features);
        }
    }

    @Override
    public RuleVo findRuleByScenario(String scenarioId) {
        return this.rules.get(scenarioId);
    }

    private Map<String, Object> prepareForAviator(List<FeatureResultVo> featureResults, JSONObject parameters) {
        Map<String, Object> requestObject = new HashMap<>();
        for (FeatureResultVo featureResultVo : featureResults) {
            requestObject.put(featureResultVo.getFeatureName(), featureResultVo.getValue());
        }

        requestObject.putAll(parameters);

        return requestObject;
    }

    @Override
    public RuleResultVo execute(String scenarioId, List<FeatureResultVo> featureResults, JSONObject parameters) {
        RuleVo ruleVo = this.rules.get(scenarioId);

//        log.info("[ruleService] features {}", antifraudUtil.formatFeatureResults(featureResults));

        Map<String, Object> requestObject = prepareForAviator(featureResults, parameters);

        Boolean isHit = aviatorService.evaluate(requestObject, ruleVo.getRule());
        RuleResultVo ruleResultVo = new RuleResultVo();
        ruleResultVo.setHit(isHit);

        return ruleResultVo;
    }
}
