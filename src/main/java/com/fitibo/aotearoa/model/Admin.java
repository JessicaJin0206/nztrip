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

    private String user;
    private String pass;
    private int discount;
}
