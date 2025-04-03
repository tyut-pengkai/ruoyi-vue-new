package com.kekecha.xiantu.domain;

public class CameraToSite {
    private int siteId;
    private int type;
    private String indexCode;
    private String name;
    private String platform;
    private int platformId;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    @Override
    public String toString() {
        return "CameraToSite{" +
                "siteId=" + siteId +
                ", type=" + type +
                ", indexCode='" + indexCode + '\'' +
                ", name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", platformId='" + platformId + '\'' +
                '}';
    }
}
