package com.hsbc.web.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import com.hsbc.util.AntifraudUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * takes requests from antifraud traffic
 */
@RestController
@RequestMapping("/antifraud")
@Slf4j
public class AntifraudController {
    @Autowired
    private AntifraudService antifraudService;
    @Autowired
    private AntifraudUtil antifraudUtil;

    @GetMapping("/health")
    public AntifraudResponse health(HttpServletRequest request) {
        log.info("[antifraud] alive");

        AntifraudResponse antifraudResponse = new AntifraudResponse();
        antifraudResponse.setCode(CodeEnum.SYSTEM_HEALTH.getCode());
        antifraudResponse.setDetails(CodeEnum.SYSTEM_HEALTH.getMessage());
        return antifraudResponse;
    }

    public AntifraudResponse request(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        AntifraudResponse antifraudResponse = null;
        JSONObject payloadConverted = null;

        /**
         * payload sanity checks
         */
        try {
            payloadConverted = antifraudUtil.acquirePayload(payload);
        } catch (Exception ex) {
            log.error("[antifraud.pre] failed {}", payload != null ? JSON.toJSONString(payload) : "", ex);

            return antifraudUtil.constructArgumentsIllegalResponse();
        }

        try {
            antifraudResponse = antifraudService.process(payloadConverted);
        } catch (Exception ex) {
            log.error("[antifraud.final] failed {}", payload != null ? JSON.toJSONString(payload) : "", ex);

            return antifraudUtil.constructSystemIllegalResponse();
        }

        return antifraudResponse;
    }
}
