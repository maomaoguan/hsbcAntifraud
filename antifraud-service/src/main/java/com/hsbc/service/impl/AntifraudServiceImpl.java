package com.hsbc.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.*;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.RuleResultVo;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AntifraudServiceImpl implements AntifraudService {
    @Autowired
    private RuleService ruleService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private DataService dataService;

    @Autowired
    private AviatorService aviatorService;

    @Override
    public void init() {
        aviatorService.init();
        dataService.init();
        featureService.init();
        ruleService.init();
    }

    @Override
    public AntifraudResponse process(JSONObject payload) throws AntifraudException {
        String scenarioId = payload.getString("scenarioId");
        String accountId = payload.getString("fAccountId");
        Long eventTime = payload.getLong("fEventTime");

        RuleVo ruleVo = ruleService.findRuleByScenario(scenarioId);

        /**
         * compute features in advance, in a paralleled way
         */
        List<FeatureResultVo> featureResults = featureService.execute(ruleVo.getFeatures(), payload);

        /**
         * going through rule execution
         */
        RuleResultVo resultVo = ruleService.execute(scenarioId, featureResults, payload);

        /**
         * fraud detected, giving feedback and warnings accordingly
         */
        if (resultVo.isHit()) {
            log.info("[antifraud] fraud detected regarding account {} - hitting rule {}", accountId, ruleVo.getDisplayName());
        }

        AntifraudResponse antifraudResponse = new AntifraudResponse();
        antifraudResponse.setCode(resultVo.isHit() ? CodeEnum.REJECTED.getCode() : CodeEnum.PASSED.getCode());
        antifraudResponse.setDetails(ruleVo.getDisplayName());

        return antifraudResponse;
    }
}
