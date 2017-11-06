package com.fitibo.aotearoa.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Sku extends ModelObject {

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
    private boolean autoGenerateReferenceNumber;
    private boolean available;
    private String checkAvailabilityWebsite;
    private boolean api;

}
