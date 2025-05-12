package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.RetTask;
import org.apache.ibatis.type.Alias;

/**
 * 退货任务vo
 * @author bcp
 */
@Alias("RetTaskVo")
public class RetTaskVo extends RetTask {

    /**
     * 物料类型
     */
    private String type;

    /**
     * 原库存地点
     */
    private String storageLocation;

    /**
     * 过期日期
     */
    private String expireTime;

    /**
     *成本中心描述
     */
    private String costCenterDesc;

    /**
     *
     */
    private String LineNo;

    private String saleCode;

    private String supplierName;

    private String supplierCode;

    private String factoryCode;

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getLineNo() {
        return LineNo;
    }

    public void setLineNo(String lineNo) {
        LineNo = lineNo;
    }

    public String getCostCenterDesc() {
        return costCenterDesc;
    }

    public void setCostCenterDesc(String costCenterDesc) {
        this.costCenterDesc = costCenterDesc;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
