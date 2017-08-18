package com.fitibo.aotearoa.vo;

/**
 * Created by 11022 on 2017/8/16.
 */
public class OrderRecordVo {
    private Integer orderId;

    private String operatorType;

    private String operator;

    private String operateType;

    private String operateTime;

    private String statusChangeFrom;

    private String statusChangeTo;

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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getStatusChangeFrom() {
        return statusChangeFrom;
    }

    public void setStatusChangeFrom(String statusChangeFrom) {
        this.statusChangeFrom = statusChangeFrom;
    }

    public String getStatusChangeTo() {
        return statusChangeTo;
    }

    public void setStatusChangeTo(String statusChangeTo) {
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
