package com.hsbc.constants;

import org.apache.commons.lang3.StringUtils;

public enum StatusEnum {
    ONLINE("online"),
    DRAFTED("drafted"),
    UNKNOWN("unknown");

    private String status;

    private StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StatusEnum typeOf(String status) {
        if (StringUtils.isNotBlank(status) && StringUtils.equals(ONLINE.getStatus(), status)) {
            return ONLINE;
        } else if (StringUtils.isNotBlank(status) && StringUtils.equals(DRAFTED.getStatus(), status)) {
            return DRAFTED;
        }

        return UNKNOWN;
    }
}
