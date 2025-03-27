package com.kekecha.xiantu.domain;

public class Car {
    private int id;
    private String name;
    private String imageUrl;
    private String videoUrl;
    // 货箱长度
    private int containerLength;
    // 货箱宽度
    private int containerWidth;
    // 货箱高度
    private int containerHeight;

    // 车身长度
    private int bodyLength;
    // 车身宽度
    private int bodyWidth;
    // 车身高度
    private int bodyHeight;
    // 容积
    private int curbWeight;
    // 额定载重
    private int ratedLoadCapacity;
    // 总质量
    private int grossWeight;
    // 能源类型
    private String energyType;

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

    public int getContainerLength() {
        return containerLength;
    }

    public void setContainerLength(int containerLength) {
        this.containerLength = containerLength;
    }

    public int getContainerWidth() {
        return containerWidth;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public int getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getBodyWidth() {
        return bodyWidth;
    }

    public void setBodyWidth(int bodyWidth) {
        this.bodyWidth = bodyWidth;
    }

    public int getBodyHeight() {
        return bodyHeight;
    }

    public void setBodyHeight(int bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public int getCurbWeight() {
        return curbWeight;
    }

    public void setCurbWeight(int curbWeight) {
        this.curbWeight = curbWeight;
    }

    public int getRatedLoadCapacity() {
        return ratedLoadCapacity;
    }

    public void setRatedLoadCapacity(int ratedLoadCapacity) {
        this.ratedLoadCapacity = ratedLoadCapacity;
    }

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", containerLength=" + containerLength +
                ", containerWidth=" + containerWidth +
                ", containerHeight=" + containerHeight +
                ", bodyLength=" + bodyLength +
                ", bodyWidth=" + bodyWidth +
                ", bodyHeight=" + bodyHeight +
                ", curbWeight=" + curbWeight +
                ", ratedLoadCapacity=" + ratedLoadCapacity +
                ", grossWeight=" + grossWeight +
                ", energyType=" + energyType +
                '}';
    }
}
