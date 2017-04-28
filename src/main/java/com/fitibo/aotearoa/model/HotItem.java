package com.fitibo.aotearoa.model;

/**
 * Created by zhouqianhao on 28/04/2017.
 */
public class HotItem extends ModelObject {

    private String skuUuid;
    private String lookupUrl;
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLookupUrl() {
        return lookupUrl;
    }

    public void setLookupUrl(String lookupUrl) {
        this.lookupUrl = lookupUrl;
    }

    public String getSkuUuid() {
        return skuUuid;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid;
    }
}
