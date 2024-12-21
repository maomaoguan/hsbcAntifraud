package com.hsbc.constants;

/**
 * supported feature types
 */
public enum CodeEnum {
    ILLEGAL_ARGUMENTS(-2, "illegalArguments"),
    SYSTEM_ERROR(-1, "systemError"),
    PASSED(0, "passed"),
    REJECTED(1, "rejected"),
    REJECTED_WITHACTIONS(2, "rejectedWithActions");

    private int code;
    private String message;

    private CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
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
