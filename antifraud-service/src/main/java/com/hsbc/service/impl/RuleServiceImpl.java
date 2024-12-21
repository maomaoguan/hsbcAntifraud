package com.hsbc.service.impl;

import com.hsbc.service.AviatorService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.RuleService;
import com.hsbc.service.builder.RuleBuilder;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.RuleResultVo;
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
public class RuleServiceImpl implements RuleService {
    private HashMap<String, RuleVo> rules = new HashMap<>();

    @Value("${antifraud.ruleNames}")
    private String ruleNames;

    private final String ruleConfPrefix = "ruleconfig/";

    @Autowired
    private FileUtil fileUtil;

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
                rules.put(ruleVo.getScenarioId(), ruleVo);
            } catch (Exception ex) {
                log.error("[ruleService] unable to init rule {}", ruleToken, ex);
            }
        }
    }

    @Override
    public RuleVo findRuleByScenario(String scenarioId) {
        return this.rules.get(scenarioId);
    }

    @Override
    public RuleResultVo execute(String scenarioId, List<FeatureResultVo> featureResults) {
        RuleVo ruleVo = this.rules.get(scenarioId);

        Map<String, Object> requestObject = new HashMap<>();
        for (FeatureResultVo featureResultVo : featureResults) {
            requestObject.put(featureResultVo.getFeatureName(), featureResultVo.getValue());
        }

        Boolean isHit = aviatorService.evaluate(requestObject, ruleVo.getRule());
        RuleResultVo ruleResultVo = new RuleResultVo();
        ruleResultVo.setHit(isHit);

        return ruleResultVo;
    }
}
