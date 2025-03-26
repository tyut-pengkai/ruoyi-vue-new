package com.kekecha.xiantu.domain;

public class CameraInstance {
    String cameraName;
    String cameraIndexCode;
    int cameraType;
    String cameraTypeName;
    String capabilitySet;
    String capabilitySetName;
    String createTime;
    String updateTime;
    String gbIndexCode;

    String altitude;
    float latitude;
    float longitude;

    String encodeDevIndexCode;
    String encodeDevResourceType;
    String encodeDevResourceTypeName;

    String recordLocation;
    String recordLocationName;
    String regionIndexCode;

    String status;
    String statusName;

    String channelNo;
    String channelType;
    String channelTypeName;

    int transType;
    String transTypeName;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    public void setCameraIndexCode(String cameraIndexCode) {
        this.cameraIndexCode = cameraIndexCode;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraTypeName() {
        return cameraTypeName;
    }

    public void setCameraTypeName(String cameraTypeName) {
        this.cameraTypeName = cameraTypeName;
    }

    public String getCapabilitySet() {
        return capabilitySet;
    }

    public void setCapabilitySet(String capabilitySet) {
        this.capabilitySet = capabilitySet;
    }

    public String getCapabilitySetName() {
        return capabilitySetName;
    }

    public void setCapabilitySetName(String capabilitySetName) {
        this.capabilitySetName = capabilitySetName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getGbIndexCode() {
        return gbIndexCode;
    }

    public void setGbIndexCode(String gbIndexCode) {
        this.gbIndexCode = gbIndexCode;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getEncodeDevIndexCode() {
        return encodeDevIndexCode;
    }

    public void setEncodeDevIndexCode(String encodeDevIndexCode) {
        this.encodeDevIndexCode = encodeDevIndexCode;
    }

    public String getEncodeDevResourceType() {
        return encodeDevResourceType;
    }

    public void setEncodeDevResourceType(String encodeDevResourceType) {
        this.encodeDevResourceType = encodeDevResourceType;
    }

    public String getEncodeDevResourceTypeName() {
        return encodeDevResourceTypeName;
    }

    public void setEncodeDevResourceTypeName(String encodeDevResourceTypeName) {
        this.encodeDevResourceTypeName = encodeDevResourceTypeName;
    }

    public String getRecordLocation() {
        return recordLocation;
    }

    public void setRecordLocation(String recordLocation) {
        this.recordLocation = recordLocation;
    }

    public String getRecordLocationName() {
        return recordLocationName;
    }

    public void setRecordLocationName(String recordLocationName) {
        this.recordLocationName = recordLocationName;
    }

    public String getRegionIndexCode() {
        return regionIndexCode;
    }

    public void setRegionIndexCode(String regionIndexCode) {
        this.regionIndexCode = regionIndexCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelTypeName() {
        return channelTypeName;
    }

    public void setChannelTypeName(String channelTypeName) {
        this.channelTypeName = channelTypeName;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getTransTypeName() {
        return transTypeName;
    }

    public void setTransTypeName(String transTypeName) {
        this.transTypeName = transTypeName;
    }

    @Override
    public String toString() {
        return "CameraInstance{" +
                "cameraName='" + cameraName + '\'' +
                ", cameraIndexCode='" + cameraIndexCode + '\'' +
                ", cameraType=" + cameraType +
                ", cameraTypeName='" + cameraTypeName + '\'' +
                ", capabilitySet='" + capabilitySet + '\'' +
                ", capabilitySetName='" + capabilitySetName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", gbIndexCode='" + gbIndexCode + '\'' +
                ", altitude='" + altitude + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", encodeDevIndexCode='" + encodeDevIndexCode + '\'' +
                ", encodeDevResourceType='" + encodeDevResourceType + '\'' +
                ", encodeDevResourceTypeName='" + encodeDevResourceTypeName + '\'' +
                ", recordLocation='" + recordLocation + '\'' +
                ", recordLocationName='" + recordLocationName + '\'' +
                ", regionIndexCode='" + regionIndexCode + '\'' +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", channelNo='" + channelNo + '\'' +
                ", channelType='" + channelType + '\'' +
                ", channelTypeName='" + channelTypeName + '\'' +
                ", transType=" + transType +
                ", transTypeName='" + transTypeName + '\'' +
                '}';
    }
}
