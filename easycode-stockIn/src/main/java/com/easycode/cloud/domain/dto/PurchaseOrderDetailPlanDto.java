package com.easycode.cloud.domain.dto;

import com.weifu.cloud.domain.PurchaseOrderDetailPlan;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 采购协议计划行dto
 * @author bcp
 */
@Alias("PurchaseOrderDetailPlanDto")
public class PurchaseOrderDetailPlanDto extends PurchaseOrderDetailPlan {

    /**
     * 供应商代码
     */
    private String supplierCode;
    /**
     * 供应商时间
     */
    private Date supplierPeriod;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Date getSupplierPeriod() {
        return supplierPeriod;
    }

    public void setSupplierPeriod(Date supplierPeriod) {
        this.supplierPeriod = supplierPeriod;
    }
}
