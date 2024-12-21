package com.hsbc.vo;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.List;

public class RuleVo implements Serializable {
    /**
     * the rule name
     */
    private String name;
    /**
     * the rule name in renderable form, like in CH character
     */
    private String displayName;
    /**
     * rule status, like online, drafted, tested, and etc
     */
    private String status;

    private String scenarioId;

    /**
     * rule contents
     */
    private String rule;

    /**
     * dependent features needed for a rule
     */
    private List<FeatureVo> features;

    protected Timestamp gmtCreate;
    protected Timestamp gmtModified;
    protected String createdBy;
    protected String updatedBy;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<FeatureVo> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureVo> features) {
        this.features = features;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
