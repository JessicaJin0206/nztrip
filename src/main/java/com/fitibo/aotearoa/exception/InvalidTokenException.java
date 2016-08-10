package com.fitibo.aotearoa.exception;

/**
 * Created by qianhao.zhou on 2/22/16.
 */
public class InvalidTokenException extends AuthenticationFailureException {

    public InvalidTokenException(String token) {
        super("invalid token:" + token);
    }
}
