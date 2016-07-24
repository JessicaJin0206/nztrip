package io.qhzhou.nztrip.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class Vendor extends ModelObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String name;
    private String email;
}
