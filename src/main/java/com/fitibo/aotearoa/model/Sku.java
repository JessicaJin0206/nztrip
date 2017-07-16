package com.fitibo.aotearoa.model;

import java.util.Collections;
import java.util.List;

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

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public boolean hasPickupService() {
        return pickupService;
    }

    public void setPickupService(boolean pickupService) {
        this.pickupService = pickupService;
    }

    public String getGatheringPlace() {
        return gatheringPlace;
    }

    public void setGatheringPlace(String gatheringPlace) {
        this.gatheringPlace = gatheringPlace;
    }

    public List<SkuTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<SkuTicket> tickets) {
        this.tickets = tickets;
    }

    public boolean isPickupService() {
        return pickupService;
    }

    public int getDurationId() {
        return durationId;
    }

    public void setDurationId(int durationId) {
        this.durationId = durationId;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public String getConfirmationTime() {
        return confirmationTime;
    }

    public void setConfirmationTime(String confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public String getRescheduleCancelNotice() {
        return rescheduleCancelNotice;
    }

    public void setRescheduleCancelNotice(String rescheduleCancelNotice) {
        this.rescheduleCancelNotice = rescheduleCancelNotice;
    }

    public String getAgendaInfo() {
        return agendaInfo;
    }

    public void setAgendaInfo(String agendaInfo) {
        this.agendaInfo = agendaInfo;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getServiceInclude() {
        return serviceInclude;
    }

    public void setServiceInclude(String serviceInclude) {
        this.serviceInclude = serviceInclude;
    }

    public String getServiceExclude() {
        return serviceExclude;
    }

    public void setServiceExclude(String serviceExclude) {
        this.serviceExclude = serviceExclude;
    }

    public String getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(String extraItem) {
        this.extraItem = extraItem;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getPriceConstraint() {
        return priceConstraint;
    }

    public void setPriceConstraint(String priceConstraint) {
        this.priceConstraint = priceConstraint;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    private String uuid;
    private String name;
    private int cityId;
    private int categoryId;
    private int vendorId;
    private String description;
    private boolean pickupService;
    private String gatheringPlace;
    private int durationId;
    private List<SkuTicket> tickets = Collections.emptyList();

    private String officialWebsite;
    private String confirmationTime;
    private String rescheduleCancelNotice;
    private String agendaInfo;
    private String activityTime;
    private String openingTime;
    private String ticketInfo;
    private String serviceInclude;
    private String serviceExclude;
    private String extraItem;
    private String attention;
    private String priceConstraint;
    private String otherInfo;

}
