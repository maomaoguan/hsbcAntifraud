package com.hsbc.vo;

import java.io.Serializable;

/**
 * represents a feature results after computing
 */
public class FeatureResultVo implements Serializable {
    private int value;
    private int status;
    private String featureName;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
