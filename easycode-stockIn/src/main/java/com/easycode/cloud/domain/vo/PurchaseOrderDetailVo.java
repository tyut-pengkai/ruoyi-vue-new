package com.easycode.cloud.domain.vo;

import com.weifu.cloud.domain.PurchaseOrderDetail;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 采购单明细对象 wms_purchase_order_detail
 *
 * @author weifu
 * @date 2023-06-16
 */
@Alias("PurchaseOrderDetailVo")
public class PurchaseOrderDetailVo extends PurchaseOrderDetail
{
    /**
     * 单据类型
     */
    private String orderType;
    /**
     * 单据号
     */
    private String orderNo;

    /**
     * 送货信息
     */
    private String deliveryInfos;

    /**
     * 供应商代码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 数量（需求-已制单）
     */
    private BigDecimal qty;

    /**
     * 数量（需求-已制单）
     */
    private String billType;

    /**
     * 本日（包含本日）之前的数量（需求-已制单）
     */
    private BigDecimal priorQty;

    private Long tenantId;

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryInfos() {
        return deliveryInfos;
    }

    public void setDeliveryInfos(String deliveryInfos) {
        this.deliveryInfos = deliveryInfos;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPriorQty() {
        return priorQty;
    }

    public void setPriorQty(BigDecimal priorQty) {
        this.priorQty = priorQty;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
}
