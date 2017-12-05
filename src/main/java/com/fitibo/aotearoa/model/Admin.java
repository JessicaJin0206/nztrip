package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public class Admin extends ModelObject {

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public final boolean isActive() {
        return status > 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String user;
    private String pass;
    private int discount;
    private int status;
}
