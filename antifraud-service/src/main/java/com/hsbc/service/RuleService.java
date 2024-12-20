package com.hsbc.service;

import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.RuleResultVo;
import com.hsbc.vo.RuleVo;

import java.util.List;

/**
 * giving all functionalities regarding rule management and executions
 */
public interface RuleService {
    public RuleVo findRuleByScenario(String scenarioId);

    public RuleResultVo execute(String scenarioId, List<FeatureResultVo> featureResults);
}
