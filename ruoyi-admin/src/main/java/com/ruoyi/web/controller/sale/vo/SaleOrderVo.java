package com.ruoyi.web.controller.sale.vo;

public class SaleOrderVo {
    private Long appId;
    private Long templateId;
    private String contact;
    private String queryPass;
    private Integer buyNum;
    private String payMode;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

//    public String getQueryPass() {
//        return queryPass;
//    }
//
//    public void setQueryPass(String queryPass) {
//        this.queryPass = queryPass;
//    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }
}
