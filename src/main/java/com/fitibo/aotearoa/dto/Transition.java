package com.fitibo.aotearoa.dto;

/**
 * Created by qianhao.zhou on 8/25/16.
 */
public final class Transition {

    public Transition(int to, String action) {
        this.to = to;
        this.action = action;
    }

    public int getTo() {
        return to;
    }

    public String getAction() {
        return action;
    }

    private final int to;
    private final String action;
}
