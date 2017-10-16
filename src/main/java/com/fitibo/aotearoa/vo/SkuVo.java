package com.fitibo.aotearoa.vo;

import java.util.List;

import lombok.Data;

/**
 * Created by qianhao.zhou on 7/29/16.
 */
@Data
public class SkuVo {

    private int id;
    private String uuid;
    private String name;
    private int cityId;
    private String city;
    private String cityEn;
    private int categoryId;
    private String category;
    private int vendorId;
    private String vendor;
    private List<String> gatheringPlace;
    private boolean pickupService;
    private String description;
    private int durationId;
    private String duration;
    private List<SkuTicketVo> tickets;

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
    private boolean autoGenerateReferenceNumber;
    private boolean available;
    private String checkAvailabilityWebsite;
}
