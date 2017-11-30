package com.fitibo.aotearoa.exception;

public class VendorEmailEmptyException extends RuntimeException {
    public VendorEmailEmptyException() {
    }

    public VendorEmailEmptyException(String message) {
        super(message);
    }

    public VendorEmailEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public VendorEmailEmptyException(Throwable cause) {
        super(cause);
    }

    public VendorEmailEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
