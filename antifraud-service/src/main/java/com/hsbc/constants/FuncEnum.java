package com.hsbc.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * func used as primary computing type in a feature computation
 */
public enum FuncEnum {
    COUNT("count"),
    SUM("sum"),
    UNKNOWN("unknown");

    private String funcType;

    private FuncEnum(String funcType) {
        this.funcType = funcType;
    }

    public String getFuncType() {
        return funcType;
    }

    public static FuncEnum typeOf(String funcType) {
        if (StringUtils.isNotBlank(funcType) && StringUtils.equals(COUNT.getFuncType(), funcType)) {
            return COUNT;
        } else if (StringUtils.isNotBlank(funcType) && StringUtils.equals(SUM.getFuncType(), funcType)) {
            return SUM;
        }

        return UNKNOWN;
    }
}
