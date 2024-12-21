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



}
