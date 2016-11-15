package com.fitibo.aotearoa.dto;

/**
 * Created by qianhao.zhou on 8/25/16.
 */
public final class Transition {

    public Transition(int to, String action, String actionEn) {
        this.to = to;
        this.action = action;
        this.actionEn = actionEn;
    }

    public int getTo() {
        return to;
    }

    public String getAction() {
        return action;
    }

    public String getActionEn() {
        return actionEn;
    }

    private final int to;
    private final String action;
    private final String actionEn;
}
