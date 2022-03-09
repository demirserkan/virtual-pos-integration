package com.trendyol.posintegrationapi.exception;

public class PosException extends Exception {
    public PosException() {}

    public PosException(String message) {
        super(message);
    }

    public PosException(String message, Throwable cause) {
        super(message, cause);
    }
}
