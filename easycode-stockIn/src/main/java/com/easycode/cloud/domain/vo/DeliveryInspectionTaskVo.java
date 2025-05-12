package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import com.easycode.cloud.domain.DeliveryInspectionTask;
import org.apache.ibatis.type.Alias;

/**
 * 送检任务实体类
 */
@Alias("DeliveryInspectionTaskVo")
public class DeliveryInspectionTaskVo extends DeliveryInspectionTask {

    private static final long serialVersionUID = 1L;

    /**
     * 仓位地点
     */
    private String positionNo;

    /**
     * 库存地点
     */
    private String locationCode;

    /**
     * 送检单单号
     */
    private String orderNo;

    private String factoryCode;


    private String positionNoPrint;

    public String getPositionNoPrint() {
        return positionNoPrint;
    }

    public void setPositionNoPrint(String positionNoPrint) {
        this.positionNoPrint = positionNoPrint;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
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
}
