package io.qhzhou.nztrip.model;

import java.util.Date;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuPrice extends ModelObject {

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getAdultCostPrice() {
        return adultCostPrice;
    }

    public void setAdultCostPrice(int adultCostPrice) {
        this.adultCostPrice = adultCostPrice;
    }

    public int getAdultSalePrice() {
        return adultSalePrice;
    }

    public void setAdultSalePrice(int adultSalePrice) {
        this.adultSalePrice = adultSalePrice;
    }

    public int getChildCostPrice() {
        return childCostPrice;
    }

    public void setChildCostPrice(int childCostPrice) {
        this.childCostPrice = childCostPrice;
    }

    public int getChildSalePrice() {
        return childSalePrice;
    }

    public void setChildSalePrice(int childSalePrice) {
        this.childSalePrice = childSalePrice;
    }

    public int getBabyCostPrice() {
        return babyCostPrice;
    }

    public void setBabyCostPrice(int babyCostPrice) {
        this.babyCostPrice = babyCostPrice;
    }

    public int getBabySalePrice() {
        return babySalePrice;
    }

    public void setBabySalePrice(int babySalePrice) {
        this.babySalePrice = babySalePrice;
    }

    public int getElderCostPrice() {
        return elderCostPrice;
    }

    public void setElderCostPrice(int elderCostPrice) {
        this.elderCostPrice = elderCostPrice;
    }

    public int getElderSalePrice() {
        return elderSalePrice;
    }

    public void setElderSalePrice(int elderSalePrice) {
        this.elderSalePrice = elderSalePrice;
    }

    public int getFamilyCostPrice() {
        return familyCostPrice;
    }

    public void setFamilyCostPrice(int familyCostPrice) {
        this.familyCostPrice = familyCostPrice;
    }

    public int getFamilySalePrice() {
        return familySalePrice;
    }

    public void setFamilySalePrice(int familySalePrice) {
        this.familySalePrice = familySalePrice;
    }

    private int skuId;
    private Date startTime;
    private int adultCostPrice;
    private int adultSalePrice;
    private int childCostPrice;
    private int childSalePrice;
    private int babyCostPrice;
    private int babySalePrice;
    private int elderCostPrice;
    private int elderSalePrice;
    private int familyCostPrice;
    private int familySalePrice;
}
