package com.kekecha.xiantu.domain;

public class Car {
    private String name;
    private String imageUrl;

    /* 基本信息 */
    // 驾驶证
    private String license;
    // 类型
    private int carType;
    // 驱动形式
    private String driveType;
    // 轴距
    private int wheelbase;
    // 箱长级别
    private int boxLengthClass;
    // 车身长度
    private int bodyLength;
    // 车身宽度
    private int bodyWidth;
    // 车身高度
    private int bodyHeight;
    // 总质量
    private int grossWeight;
    // 额定载重
    private int ratedLoadCapacity;
    // 整车重量
    private int curbWeight;
    // 最高车速
    private int maximumSpeed;
    // 吨位级别
    private int tonnageClass;
    // 产地
    private String placeOfOrigin;
    // 换电支持
    private boolean batterySwapSupport;
    // 能源类型
    private int energyType;

    /* 电机 */
    // 后电机品牌
    private String rearMotorBrand;
    // 后电机型号
    private String rearMotorModel;
    // 电机型式
    private int motorType;
    // 总额定功率
    private int totalRatedPower;
    // 峰值功率
    private int peakPower;
    //总峰值扭矩
    private int totalPeakTorque;
    // 燃料类别
    private String fuelCategory;

    /* 驾驶室参数 */
    // 驾驶室宽度
    private int cabWidth;
    // 准乘人数
    private int cabPassengerNum;
    // 座位排数
    private int cabRowNum;

    /* 底盘参数 */
    // 前桥允许载荷
    private int chassisFrontLoad;
    // 后桥允许载荷
    private int chassisRearLoad;

    /* 货箱参数 */
    // 货箱形式
    private int containerType;
    // 货箱长度
    private int containerLength;
    // 货箱宽度
    private int containerWidth;
    // 货箱高度
    private int containerHeight;

    /* 轮胎 */
    // 轮胎规格
    private String tireSize;
    // 轮胎数
    private int tireNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public int getWheelbase() {
        return wheelbase;
    }

    public void setWheelbase(int wheelbase) {
        this.wheelbase = wheelbase;
    }

    public int getBoxLengthClass() {
        return boxLengthClass;
    }

    public void setBoxLengthClass(int boxLengthClass) {
        this.boxLengthClass = boxLengthClass;
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

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getRatedLoadCapacity() {
        return ratedLoadCapacity;
    }

    public void setRatedLoadCapacity(int ratedLoadCapacity) {
        this.ratedLoadCapacity = ratedLoadCapacity;
    }

    public int getCurbWeight() {
        return curbWeight;
    }

    public void setCurbWeight(int curbWeight) {
        this.curbWeight = curbWeight;
    }

    public int getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(int maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public int getTonnageClass() {
        return tonnageClass;
    }

    public void setTonnageClass(int tonnageClass) {
        this.tonnageClass = tonnageClass;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public boolean isBatterySwapSupport() {
        return batterySwapSupport;
    }

    public void setBatterySwapSupport(boolean batterySwapSupport) {
        this.batterySwapSupport = batterySwapSupport;
    }

    public int getEnergyType() {
        return energyType;
    }

    public void setEnergyType(int energyType) {
        this.energyType = energyType;
    }

    public String getRearMotorBrand() {
        return rearMotorBrand;
    }

    public void setRearMotorBrand(String rearMotorBrand) {
        this.rearMotorBrand = rearMotorBrand;
    }

    public String getRearMotorModel() {
        return rearMotorModel;
    }

    public void setRearMotorModel(String rearMotorModel) {
        this.rearMotorModel = rearMotorModel;
    }

    public int getMotorType() {
        return motorType;
    }

    public void setMotorType(int motorType) {
        this.motorType = motorType;
    }

    public int getTotalRatedPower() {
        return totalRatedPower;
    }

    public void setTotalRatedPower(int totalRatedPower) {
        this.totalRatedPower = totalRatedPower;
    }

    public int getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(int peakPower) {
        this.peakPower = peakPower;
    }

    public int getTotalPeakTorque() {
        return totalPeakTorque;
    }

    public void setTotalPeakTorque(int totalPeakTorque) {
        this.totalPeakTorque = totalPeakTorque;
    }

    public String getFuelCategory() {
        return fuelCategory;
    }

    public void setFuelCategory(String fuelCategory) {
        this.fuelCategory = fuelCategory;
    }

    public int getCabWidth() {
        return cabWidth;
    }

    public void setCabWidth(int cabWidth) {
        this.cabWidth = cabWidth;
    }

    public int getCabPassengerNum() {
        return cabPassengerNum;
    }

    public void setCabPassengerNum(int cabPassengerNum) {
        this.cabPassengerNum = cabPassengerNum;
    }

    public int getCabRowNum() {
        return cabRowNum;
    }

    public void setCabRowNum(int cabRowNum) {
        this.cabRowNum = cabRowNum;
    }

    public int getChassisFrontLoad() {
        return chassisFrontLoad;
    }

    public void setChassisFrontLoad(int chassisFrontLoad) {
        this.chassisFrontLoad = chassisFrontLoad;
    }

    public int getChassisRearLoad() {
        return chassisRearLoad;
    }

    public void setChassisRearLoad(int chassisRearLoad) {
        this.chassisRearLoad = chassisRearLoad;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
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

    public String getTireSize() {
        return tireSize;
    }

    public void setTireSize(String tireSize) {
        this.tireSize = tireSize;
    }

    public int getTireNum() {
        return tireNum;
    }

    public void setTireNum(int tireNum) {
        this.tireNum = tireNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", license='" + license + '\'' +
                ", carType=" + carType +
                ", driveType='" + driveType + '\'' +
                ", wheelbase=" + wheelbase +
                ", boxLengthClass=" + boxLengthClass +
                ", bodyLength=" + bodyLength +
                ", bodyWidth=" + bodyWidth +
                ", bodyHeight=" + bodyHeight +
                ", grossWeight=" + grossWeight +
                ", ratedLoadCapacity=" + ratedLoadCapacity +
                ", curbWeight=" + curbWeight +
                ", maximumSpeed=" + maximumSpeed +
                ", tonnageClass=" + tonnageClass +
                ", placeOfOrigin='" + placeOfOrigin + '\'' +
                ", batterySwapSupport=" + batterySwapSupport +
                ", energyType=" + energyType +
                ", rearMotorBrand='" + rearMotorBrand + '\'' +
                ", rearMotorModel='" + rearMotorModel + '\'' +
                ", motorType=" + motorType +
                ", totalRatedPower=" + totalRatedPower +
                ", peakPower=" + peakPower +
                ", totalPeakTorque=" + totalPeakTorque +
                ", fuelCategory='" + fuelCategory + '\'' +
                ", cabWidth=" + cabWidth +
                ", cabPassengerNum=" + cabPassengerNum +
                ", cabRowNum=" + cabRowNum +
                ", chassisFrontLoad=" + chassisFrontLoad +
                ", chassisRearLoad=" + chassisRearLoad +
                ", containerType=" + containerType +
                ", containerLength=" + containerLength +
                ", containerWidth=" + containerWidth +
                ", containerHeight=" + containerHeight +
                ", tireSize='" + tireSize + '\'' +
                ", tireNum=" + tireNum +
                '}';
    }
}
