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

    private String uuid;
    private String name;
    private int cityId;
    private int categoryId;
    private int vendorId;
    private String description;
    private boolean pickupService;
    private String gatheringPlace;
    private List<SkuTicket> tickets = Collections.emptyList();

}
