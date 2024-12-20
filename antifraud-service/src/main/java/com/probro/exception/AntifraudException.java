package com.probro.exception;

public class AntifraudException extends Exception{
    private int code;
    private String message;

    public AntifraudException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
