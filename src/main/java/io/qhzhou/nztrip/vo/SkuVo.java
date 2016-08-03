package io.qhzhou.nztrip.vo;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/29/16.
 */
public class SkuVo {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean hasPickupService() {
        return pickupService;
    }

    public void setPickupService(boolean pickupService) {
        this.pickupService = pickupService;
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

    public List<String> getGatheringPlace() {
        return gatheringPlace;
    }

    public void setGatheringPlace(List<String> gatheringPlace) {
        this.gatheringPlace = gatheringPlace;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SkuTicketVo> getTickets() {
        return tickets;
    }
    public void setTickets(List<SkuTicketVo> tickets) {
        this.tickets = tickets;
    }

    private int id;
    private String uuid;
    private String name;
    private int cityId;
    private String city;
    private int categoryId;
    private String category;
    private int vendorId;
    private List<String> gatheringPlace;
    private boolean pickupService;
    private String description;
    private List<SkuTicketVo> tickets;
}
