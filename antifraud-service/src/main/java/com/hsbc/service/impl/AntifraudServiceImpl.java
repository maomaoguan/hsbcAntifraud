package com.hsbc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import com.hsbc.service.FeatureService;
import com.hsbc.service.RuleService;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.RuleResultVo;
import com.hsbc.vo.RuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntifraudServiceImpl implements AntifraudService {
    @Autowired
    private RuleService ruleService;

    @Autowired
    private FeatureService featureService;

    @Override
    public AntifraudResponse process(JSONObject payload) throws AntifraudException {
        String scenarioId = payload.getString("scenarioId");
        String userId = payload.getString("userId");
        JSONObject parameters = payload.getJSONObject("parameters");

        RuleVo ruleVo = ruleService.findRuleByScenario(scenarioId);

        /**
         * compute features in advance, in a paralleled way
         */
        List<FeatureResultVo> featureResults = featureService.execute(ruleVo.getFeatures(), parameters);

        /**
         * going through rule execution
         */
        RuleResultVo resultVo = ruleService.execute(scenarioId, featureResults);

        AntifraudResponse antifraudResponse = new AntifraudResponse();
        antifraudResponse.setCode(resultVo.isHit() ? CodeEnum.PASSED.getCode() : CodeEnum.REJECTED.getCode());

        return antifraudResponse;
    }
}
