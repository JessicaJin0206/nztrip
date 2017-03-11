package com.fitibo.aotearoa.model;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
public class Attachment extends ModelObject {

    private int emailId;
    private byte[] data;
    private String name;

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
