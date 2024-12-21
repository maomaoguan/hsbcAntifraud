package com.hsbc.exception;

/**
 * exception when doing feature or rule computations
 */
public class ComputeException extends Exception {
    private int code;
    private String message;

    public ComputeException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public ComputeException(String message, int code) {
        this.message = message;
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
