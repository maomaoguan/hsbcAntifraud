package com.hsbc.response;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * the major response regarding a request of antifraud detection
 */
public class AntifraudResponse implements Serializable {
    private int code;
    private JSONObject details;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JSONObject getDetails() {
        return details;
    }

    public void setDetails(JSONObject details) {
        this.details = details;
    }
}
