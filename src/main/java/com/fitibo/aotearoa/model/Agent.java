package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class Agent extends ModelObject {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDefaultContact() {
        return defaultContact;
    }

    public void setDefaultContact(String defaultContact) {
        this.defaultContact = defaultContact;
    }

    public String getDefaultContactEmail() {
        return defaultContactEmail;
    }

    public void setDefaultContactEmail(String defaultContactEmail) {
        this.defaultContactEmail = defaultContactEmail;
    }

    private String userName;
    private String password;
    private String name;
    private String description;
    private int discount = 100;
    private String email;
    private String defaultContact;
    private String defaultContactEmail;

}
