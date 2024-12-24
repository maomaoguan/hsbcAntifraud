package com.hsbc.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@Service
public class MockUtils {

    private final List<String> scenarios = new ArrayList<>();

    private final List<String> accounts = new ArrayList<>();

    private final String maliciousAccount = "account3";

    private final String maliciousScenario = "scenario2";

    @PostConstruct
    private void init() {
        scenarios.add("scenario1");
        scenarios.add("scenario2");
        scenarios.add("scenario3");

        accounts.add("account1");
        accounts.add("account2");
        accounts.add("account4");
        accounts.add("account5");
    }

    public JSONObject mockPayload1() {
        JSONObject payload = new JSONObject();

        payload.put("scenarioId", "scenario1");
        payload.put("fAccountId", "account1");
        payload.put("fEventTime", System.currentTimeMillis());

        return payload;
    }

    /**
     * used in integration test which is mockings to real fraud requests;
     * mock data in batched form
     *
     * @return
     */
    public List<JSONObject> mockRealPayload() {
        List<JSONObject> payloads = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            JSONObject payload = new JSONObject();
            payloads.add(payload);

            int randScenario = RandomUtils.nextInt(0, this.scenarios.size());
            int randAccount = RandomUtils.nextInt(0, this.accounts.size());

            String scenarioId = scenarios.get(randScenario);
            String accountId = accounts.get(randAccount);

            /**
             * 1/50 chance for giving a real fraud
             */
            if (RandomUtils.nextInt(0, 20) == 0) {
                accountId = maliciousAccount;
                scenarioId = maliciousScenario;
            }

            payload.put("scenarioId", scenarioId);
            payload.put("fAccountId", accountId);
            payload.put("fEventTime", System.currentTimeMillis());
        }

        return payloads;
    }

    public HttpServletRequest mockHttpRequest() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "test");

        return httpServletRequest;
    }
}
