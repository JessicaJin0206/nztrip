package com.fitibo.aotearoa.exception;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public class InvalidParamException extends RuntimeException {

    public InvalidParamException() {
    }

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamException(Throwable cause) {
        super(cause);
    }

    public InvalidParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
