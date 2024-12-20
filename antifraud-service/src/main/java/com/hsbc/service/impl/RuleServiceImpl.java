package com.hsbc.service.impl;

import com.hsbc.service.RuleService;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.RuleResultVo;
import com.hsbc.vo.RuleVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {
    private HashMap<String, RuleVo> scenarioRules = new HashMap<>();

    private void loadRules() {

    }

    @Override
    public RuleVo findRuleByScenario(String scenarioId) {
        return null;
    }

    @Override
    public RuleResultVo execute(String scenarioId, List<FeatureResultVo> featureResults) {
        return null;
    }
}
