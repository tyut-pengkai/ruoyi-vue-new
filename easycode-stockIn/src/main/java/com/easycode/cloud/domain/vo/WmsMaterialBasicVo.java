package com.easycode.cloud.domain.vo;

import com.weifu.cloud.domian.WmsMaterialBasic;
import org.apache.ibatis.type.Alias;

/**
 * 物料基础信息 出参数据vo
 * @author bcp
 */
@Alias("WmsMaterialBasicVo")
public class WmsMaterialBasicVo extends WmsMaterialBasic {
    /** 工厂代码对应的id */
    private Long factoryId;

    /** 货主（工厂代码）*/
    private String factoryCode;

    /** 库存地点id*/
    private String locationId;
    /** 库存地点code*/
    private String locationCode;

    /**
     * 物料属性 物料类型 1 原材料 2半成品 3成品
     */
    private String type;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 慢流动物料(1是 0否)
     */
    private String slowFlow;
    /**
     * 补料包装数量
     */
    private String packageCount;

    public String getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(String packageCount) {
        this.packageCount = packageCount;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSlowFlow() {
        return slowFlow;
    }

    public void setSlowFlow(String slowFlow) {
        this.slowFlow = slowFlow;
    }
}

