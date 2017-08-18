package com.fitibo.aotearoa.model;

import java.util.Date;

public class OrderRecord extends ModelObject {

    private Integer orderId;

    private String operatorType;

    private Integer operatorId;

    private String operateType;

    private Date operateTime;

    private Integer statusChangeFrom;

    private Integer statusChangeTo;

    private String contentChangeFrom;

    private String contentChangeTo;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getStatusChangeFrom() {
        return statusChangeFrom;
    }

    public void setStatusChangeFrom(Integer statusChangeFrom) {
        this.statusChangeFrom = statusChangeFrom;
    }

    public Integer getStatusChangeTo() {
        return statusChangeTo;
    }

    public void setStatusChangeTo(Integer statusChangeTo) {
        this.statusChangeTo = statusChangeTo;
    }

    public String getContentChangeFrom() {
        return contentChangeFrom;
    }

    public void setContentChangeFrom(String contentChangeFrom) {
        this.contentChangeFrom = contentChangeFrom;
    }

    public String getContentChangeTo() {
        return contentChangeTo;
    }

    public void setContentChangeTo(String contentChangeTo) {
        this.contentChangeTo = contentChangeTo;
    }
}