package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.dto.Token;

/**
 * Created by qianhao.zhou on 8/18/16.
 */
public class AuthenticationRequiredController {

    private ThreadLocal<Token> token = new ThreadLocal<>();

    public void setToken(Token token) {
        this.token.set(token);
    }

    public Token getToken() {
        return this.token.get();
    }
}
