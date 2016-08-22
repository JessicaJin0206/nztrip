package com.fitibo.aotearoa.exception;

/**
 * Created by qianhao.zhou on 8/13/16.
 */
public class InvalidDateFormatException extends InvalidParamException {

    public InvalidDateFormatException() {
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }

    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidDateFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
