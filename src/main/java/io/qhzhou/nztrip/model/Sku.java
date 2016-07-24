package io.qhzhou.nztrip.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class Sku extends ModelObject {

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasAdultTicket() {
        return adultTicket;
    }

    public void setAdultTicket(boolean adultTicket) {
        this.adultTicket = adultTicket;
    }

    public String getAdultTicketRemark() {
        return adultTicketRemark;
    }

    public void setAdultTicketRemark(String adultTicketRemark) {
        this.adultTicketRemark = adultTicketRemark;
    }

    public boolean hasChildTicket() {
        return childTicket;
    }

    public void setChildTicket(boolean childTicket) {
        this.childTicket = childTicket;
    }

    public String getChildTicketRemark() {
        return childTicketRemark;
    }

    public void setChildTicketRemark(String childTicketRemark) {
        this.childTicketRemark = childTicketRemark;
    }

    public boolean hasBabyTicket() {
        return babyTicket;
    }

    public void setBabyTicket(boolean babyTicket) {
        this.babyTicket = babyTicket;
    }

    public String getBabyTicketRemark() {
        return babyTicketRemark;
    }

    public void setBabyTicketRemark(String babyTicketRemark) {
        this.babyTicketRemark = babyTicketRemark;
    }

    public boolean hasElderTicket() {
        return elderTicket;
    }

    public void setElderTicket(boolean elderTicket) {
        this.elderTicket = elderTicket;
    }

    public String getElderTicketRemark() {
        return elderTicketRemark;
    }

    public void setElderTicketRemark(String elderTicketRemark) {
        this.elderTicketRemark = elderTicketRemark;
    }

    public boolean hasFamilyTicket() {
        return familyTicket;
    }

    public void setFamilyTicket(boolean familyTicket) {
        this.familyTicket = familyTicket;
    }

    public String getFamilyTicketRemark() {
        return familyTicketRemark;
    }

    public void setFamilyTicketRemark(String familyTicketRemark) {
        this.familyTicketRemark = familyTicketRemark;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    private String uuid;
    private String name;
    private int cityId;
    private int categoryId;
    private String description;
    private boolean adultTicket;
    private String adultTicketRemark;
    private boolean childTicket;
    private String childTicketRemark;
    private boolean babyTicket;
    private String babyTicketRemark;
    private boolean elderTicket;
    private String elderTicketRemark;
    private boolean familyTicket;
    private String familyTicketRemark;
    private int vendorId;
}
