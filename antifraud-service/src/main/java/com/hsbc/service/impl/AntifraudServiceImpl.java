package com.hsbc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.mns.model.Message;
import com.hsbc.constants.CodeEnum;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.*;
import com.hsbc.util.AntifraudUtil;
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
    @Autowired
    private AntifraudUtil antifraudUtil;

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

        long begTime = System.currentTimeMillis();
        RuleVo ruleVo = ruleService.findRuleByScenario(scenarioId);

        if (ruleVo == null) {
            throw new AntifraudException(CodeEnum.ILLEGAL_ARGUMENTS.getMessage(), CodeEnum.ILLEGAL_ARGUMENTS.getCode());
        }

        /**
         * compute features in advance, in a paralleled way
         */
        List<FeatureResultVo> featureResults = featureService.execute(ruleVo.getFeatures(), payload);

        /**
         * going through rule execution
         */
        RuleResultVo resultVo = ruleService.execute(scenarioId, featureResults, payload);

        AntifraudResponse antifraudResponse = new AntifraudResponse();
        antifraudResponse.setAccountId(accountId);
        antifraudResponse.setCode(resultVo.isHit() ? CodeEnum.REJECTED.getCode() : CodeEnum.PASSED.getCode());
        if (resultVo.isHit()) {
            antifraudResponse.setDetails(ruleVo.getDisplayName());
        }

        long endTime = System.currentTimeMillis();
        log.info("[antifraudService] succeed computing with cost - {}", endTime - begTime);

        return antifraudResponse;
    }

    @Override
    public AntifraudResponse process(Message rawPayload) throws AntifraudException {
        JSONObject payload = null;

        log.info("[antifraudService] payload {}", rawPayload.getMessageBody());

        try {
            payload = antifraudUtil.buildPayload(rawPayload);
        } catch (Exception ex) {
            throw new AntifraudException(ex.getMessage(), CodeEnum.ILLEGAL_ARGUMENTS.getCode(), ex);
        }

        return this.process(payload);
    }
}
