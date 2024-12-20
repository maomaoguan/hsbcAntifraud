package com.hsbc.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.response.AntifraudResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AntifraudUtil {

    /**
     * parse single or multiple keys out from summary
     *
     * @param summary
     * @return
     */
    public List<String> parseInputKeys(JSONObject summary) {
        List<String> inputKeys = new ArrayList<>(2);
        for (Map.Entry<String, Object> entry : summary.getJSONObject("input").entrySet()) {
            inputKeys.add(entry.getKey());
        }

        return inputKeys;
    }

    public boolean isSingleKey(JSONObject summary) {
        List<String> inputKeys = parseInputKeys(summary);

        return inputKeys.size() == 1;
    }

    public Map<String, Object> jsonToMap(JSONObject rawData) {
        Map<String, Object> converted = new HashMap();
        for (Map.Entry<String, Object> entry : rawData.entrySet()) {
            converted.put(entry.getKey(), entry.getValue());
        }

        return converted;
    }


    public JSONArray convertJSONArray(String payload) {
        JSONArray jsonArray = new JSONArray();
        if (payload == null) {
            return jsonArray;
        }
        if (isJSONArrayValid(payload)) {
            jsonArray = JSONArray.parseArray(payload);

            return jsonArray;
        } else if (StringUtils.equals(payload, "null")) {
            return jsonArray;
        } else {

            try {
                JSONObject jsonItem = JSONObject.parseObject(payload);
                jsonArray.add(jsonItem);
            } catch (Exception ex) {

            }
        }

        return jsonArray;
    }

    public boolean isJSONArrayValid(String payload) {
        if (StringUtils.isBlank(payload)) {
            return false;
        }

        try {
            JSONArray jsonArray = JSONArray.parseArray(payload);

            if (jsonArray != null) {
                return true;
            }
        } catch (Exception ex) {

        }

        return false;
    }

    public boolean isJSONValid(String payload) {
        if (StringUtils.isBlank(payload)) {
            return false;
        }

        try {
            JSONObject json = JSONObject.parseObject(payload);

            if (json != null) {
                return true;
            }
        } catch (Exception ex) {
            /**
             * intentionally ignore throwable
             */
            try {
                JSONArray jsonArray = JSONArray.parseArray(payload);

                if (jsonArray != null) {
                    return true;
                }
            } catch (Exception ex2) {
            }
        }

        return false;
    }

    public String convertToInputList(JSONObject summary) {
        JSONArray inputList = new JSONArray();

        if (summary == null || summary.getJSONObject("input") == null) {
            return "";
        }

        for (Map.Entry<String, Object> entry : summary.getJSONObject("input").entrySet()) {
            JSONObject input = new JSONObject();
            input.put("field", entry.getKey());
            input.put("type", String.valueOf(entry.getValue()));
            inputList.add(input);
        }

        return inputList.toJSONString();
    }

    public String generateScope(List<String> projects) {
        StringBuilder scopes = new StringBuilder();

        for (String project : projects) {
            scopes.append(project);
            scopes.append(",");
        }

        return scopes.toString();
    }

    public boolean isMultipleFeatures(String payload) {
        JSONObject payloadJson = JSONObject.parseObject(payload);

        if (payloadJson.getJSONObject("summary") != null) {
            JSONObject output = payloadJson.getJSONObject("summary").getJSONObject("output");

            return output != null && output.size() > 1;
        }

        return false;
    }

    public String standardizeString(Object param) throws RuntimeException {
        String standardContent = "";
        if (param == null) {
            return standardContent;
        }

        if (String.class.isAssignableFrom(param.getClass())) {
            standardContent = (String) param;
        } else if (Integer.class.isAssignableFrom(param.getClass())) {
            standardContent = String.valueOf((Integer) param);
        } else if (Double.class.isAssignableFrom(param.getClass())) {
            standardContent = String.valueOf((Double) param);
        } else {
            throw new RuntimeException(String.format("[commonutil] illegal param encountered %s", param != null ? param.getClass().toString() : ""));
        }

        return StringUtils.trim(standardContent);
    }

    /**
     * checks on mandatory parameters and convert into internal format
     *
     * @param rawPayload
     * @return
     */
    public JSONObject acquirePayload(Map<String, Object> rawPayload) throws Exception {
        JSONObject payload = new JSONObject();
        if (rawPayload.containsKey("userId")) {
            payload.put("userId", rawPayload.get("userId"));
        } else {
            throw new Exception("mandatory field not found, no userId found");
        }

        if (rawPayload.containsKey("scenarioId")) {
            payload.put("scenarioId", rawPayload.get("scenarioId"));
        } else {
            throw new Exception("mandatory field not found, no scenarioId found");
        }

        return payload;
    }

    public AntifraudResponse constructArgumentsIllegalResponse() {
        AntifraudResponse antifraudResponse = new AntifraudResponse();

        antifraudResponse.setCode(CodeEnum.ILLEGAL_ARGUMENTS.getCode());

        return antifraudResponse;
    }


    public AntifraudResponse constructSystemIllegalResponse() {
        AntifraudResponse antifraudResponse = new AntifraudResponse();

        antifraudResponse.setCode(CodeEnum.SYSTEM_ERROR.getCode());

        return antifraudResponse;
    }

}