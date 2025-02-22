package com.kekecha.xiantu.domain;

public class Camera {
    private String name;
    private String supplier;
    private String appKey;
    private String appSecret;
    private int netProtocol;
    private String platformUrl;
    private int streamProtocol;
    private int recordProtocol;
    private String description;

    private String refSite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getNetProtocol() {
        return netProtocol;
    }

    public void setNetProtocol(int netProtocol) {
        this.netProtocol = netProtocol;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    public int getStreamProtocol() {
        return streamProtocol;
    }

    public void setStreamProtocol(int streamProtocol) {
        this.streamProtocol = streamProtocol;
    }

    public int getRecordProtocol() {
        return recordProtocol;
    }

    public void setRecordProtocol(int recordProtocol) {
        this.recordProtocol = recordProtocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefSite() {
        return refSite;
    }

    public void setRefSite(String refSite) {
        this.refSite = refSite;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "name='" + name + '\'' +
                ", supplier='" + supplier + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", netProtocol=" + netProtocol +
                ", platformUrl='" + platformUrl + '\'' +
                ", streamProtocol=" + streamProtocol +
                ", recordProtocol=" + recordProtocol +
                ", description='" + description + '\'' +
                ", refSite='" + refSite + '\'' +
                '}';
    }
}
