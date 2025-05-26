package com.kekecha.xiantu.domain;

public class Car {
    private int id;
    private String name;
    private String imageUrl;
    private String videoUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    private String coverUrl;
    // 货箱长度
    private float containerLength;
    // 货箱宽度
    private float containerWidth;
    // 货箱高度
    private float containerHeight;

    // 车身长度
    private float bodyLength;
    // 车身宽度
    private float bodyWidth;
    // 车身高度
    private float bodyHeight;
    // 容积
    private float curbWeight;
    // 额定载重
    private float ratedLoadCapacity;
    // 总质量
    private float grossWeight;
    // 能源类型
    private String energyType;

    // 油箱容量
    private float tankCapacity;
    // 电池容量
    private float batteryCapacity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public float getContainerLength() {
        return containerLength;
    }

    public void setContainerLength(float containerLength) {
        this.containerLength = containerLength;
    }

    public float getContainerWidth() {
        return containerWidth;
    }

    public void setContainerWidth(float containerWidth) {
        this.containerWidth = containerWidth;
    }

    public float getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(float containerHeight) {
        this.containerHeight = containerHeight;
    }

    public float getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(float bodyLength) {
        this.bodyLength = bodyLength;
    }

    public float getBodyWidth() {
        return bodyWidth;
    }

    public void setBodyWidth(float bodyWidth) {
        this.bodyWidth = bodyWidth;
    }

    public float getBodyHeight() {
        return bodyHeight;
    }

    public void setBodyHeight(float bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public float getCurbWeight() {
        return curbWeight;
    }

    public void setCurbWeight(float curbWeight) {
        this.curbWeight = curbWeight;
    }

    public float getRatedLoadCapacity() {
        return ratedLoadCapacity;
    }

    public void setRatedLoadCapacity(float ratedLoadCapacity) {
        this.ratedLoadCapacity = ratedLoadCapacity;
    }

    public float getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(float grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public float getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(float tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public float getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(float batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", containerLength=" + containerLength +
                ", containerWidth=" + containerWidth +
                ", containerHeight=" + containerHeight +
                ", bodyLength=" + bodyLength +
                ", bodyWidth=" + bodyWidth +
                ", bodyHeight=" + bodyHeight +
                ", curbWeight=" + curbWeight +
                ", ratedLoadCapacity=" + ratedLoadCapacity +
                ", grossWeight=" + grossWeight +
                ", energyType='" + energyType + '\'' +
                ", tankCapacity=" + tankCapacity +
                ", batteryCapacity=" + batteryCapacity +
                '}';
    }
}
