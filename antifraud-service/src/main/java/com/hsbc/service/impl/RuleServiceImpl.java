package com.hsbc.service.impl;

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

@Service
@Slf4j
public class RuleServiceImpl implements RuleService {
    private HashMap<String, RuleVo> scenarioRules = new HashMap<>();

    @Value("${antifraud.rules}")
    private String rules;

    private final String ruleConfPrefix = "ruleconfig/";

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RuleBuilder ruleBuilder;

    @Override
    public void init() {
        String[] ruleTokens = StringUtils.split(rules, ",");
        for (String ruleToken : ruleTokens) {
            try {
                String rawContent = fileUtil.loadFile(ruleConfPrefix + StringUtils.trim(ruleToken));
                RuleVo ruleVo = ruleBuilder.buildRule(rawContent);
                scenarioRules.put(ruleVo.getScenarioId(), ruleVo);
            } catch (Exception ex) {
                log.error("[ruleService] unable to init rule {}", ruleToken, ex);
            }
        }
    }


    @Override
    public RuleVo findRuleByScenario(String scenarioId) {
        return this.scenarioRules.get(scenarioId);
    }

    @Override
    public RuleResultVo execute(String scenarioId, List<FeatureResultVo> featureResults) {
        return null;
    }
}
