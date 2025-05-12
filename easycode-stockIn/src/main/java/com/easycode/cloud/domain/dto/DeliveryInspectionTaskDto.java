package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.DeliveryInspectionTask;
import org.apache.ibatis.type.Alias;

/**
 * 送检任务实体类
 */
@Alias("DeliveryInspectionTaskDto")
public class DeliveryInspectionTaskDto extends DeliveryInspectionTask {

    private static final long serialVersionUID = 1L;

    /**
     * 库存地点
     */
    private String locationCode;

    /**
     * 送检单单号
     */
    private String orderNo;

    private String startTime;

    private String endTime;

    private String factoryCode;

    private String positionNo;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }
}
