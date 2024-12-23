package com.hsbc.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * supported feature types
 */
public enum CodeEnum {
    ILLEGAL_ARGUMENTS(-2, "illegalArguments"),
    SYSTEM_ERROR(-1, "systemError"),
    PASSED(0, "passed"),
    REJECTED(1, "rejected"),
    SYSTEM_HEALTH(101, "healthy"),
    REJECTED_WITHACTIONS(2, "rejectedWithActions"),
    UNKNOWN(404, "unknown");


    private int code;
    private String message;

    private CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CodeEnum typeOf(int code) {
        if (code == PASSED.getCode()) {
            return PASSED;
        } else if (code == REJECTED.getCode()) {
            return REJECTED;
        }

        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
