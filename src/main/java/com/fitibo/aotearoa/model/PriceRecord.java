package com.fitibo.aotearoa.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qianhao.zhou on 18/12/2016.
 */
public class PriceRecord extends ModelObject {

    private String company;
    private String url;
    private String category;
    private BigDecimal price;
    private Date createTime;
    private String sku;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
