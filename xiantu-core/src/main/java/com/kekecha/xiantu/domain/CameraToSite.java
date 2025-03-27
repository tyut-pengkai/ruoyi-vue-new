package com.kekecha.xiantu.domain;

public class CameraToSite {
    private int siteId;
    private int type;
    private String indexCode;

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

    @Override
    public String toString() {
        return "CameraToSite{" +
                "siteId=" + siteId +
                ", type=" + type +
                ", indexCode='" + indexCode + '\'' +
                '}';
    }
}
