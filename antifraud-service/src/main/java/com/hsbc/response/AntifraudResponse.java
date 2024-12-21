package com.hsbc.response;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * the major response regarding a request of antifraud detection
 */
public class AntifraudResponse implements Serializable {
    private int code;
    private String details;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
