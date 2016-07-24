package io.qhzhou.nztrip.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuRemark extends ModelObject {

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int skuId;
    private String name;
    private boolean required;
    private int type;
}
