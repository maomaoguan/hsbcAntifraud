package com.hsbc.vo;

/**
 * this is a representation of all types of features, feature major fields are described while internal logic are encapsulated in transparency manner
 */
public class FeatureVo {
    /**
     * the feature name
     */
    private String name;
    /**
     * the feature name in renderable form, like in CH character
     */
    private String displayName;
    /**
     * feature status, like online, drafted, tested, and etc
     */
    private String status;

    private String funcType;

    private String fieldName;

    private String filter;

    private int period;

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

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
