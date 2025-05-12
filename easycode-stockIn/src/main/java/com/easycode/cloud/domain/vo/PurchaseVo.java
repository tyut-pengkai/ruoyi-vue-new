package com.easycode.cloud.domain.vo;

import com.weifu.cloud.domain.PurchaseOrderDetail;
import org.apache.ibatis.type.Alias;

/**
 * 采购单vo
 * @author hbh
 */
@Alias("PurchaseVo")
public class PurchaseVo extends PurchaseOrderDetail {

    /**
     * 采购单据状态
     */
    private String orderStatus;

    /**
     * 采购单据类型
     */
    private String orderType;

    private String supplierCode;

    private Long agId;

    public Long getAgId() {
        return agId;
    }

    public void setAgId(Long agId) {
        this.agId = agId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setOrderType(String orderType) {

        this.orderType = orderType;
    }
}
