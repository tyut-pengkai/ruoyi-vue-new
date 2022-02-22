package com.ruoyi.web.controller.sale.vo;

public class SaleAppVo {

    private Long appId;
    private String appName;
    private Integer tplCount;

    public SaleAppVo(Long appId, String appName, Integer tplCount) {
        this.appId = appId;
        this.appName = appName;
        this.tplCount = tplCount;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getTplCount() {
        return tplCount;
    }

    public void setTplCount(Integer tplCount) {
        this.tplCount = tplCount;
    }
}
