package com.hsbc.service.builder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.service.AviatorService;
import com.hsbc.util.AntifraudUtil;
import com.hsbc.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RuleBuilder {
    @Autowired
    private AntifraudUtil antifraudUtil;

    @Autowired
    private AviatorService aviatorService;

    public RuleVo buildRule(String rawContent) throws Exception {
        if (!antifraudUtil.isJSONValid(rawContent)) {
            throw new Exception("illegal rule json found");
        }

        JSONObject ruleJson = JSON.parseObject(rawContent);
        RuleVo ruleVo = new RuleVo();

        String scenarioId = ruleJson.getString("scenario");
        ruleVo.setScenarioId(scenarioId);
        ruleVo.setRule(ruleJson.getString("rule"));
        ruleVo.setStatus(ruleJson.getString("status"));

        /**
         * TODO: parse features later
         */
//        ruleVo.setFeatures();
        aviatorService.findVariables(ruleJson.getString("rule"));

        return ruleVo;
    }
}
