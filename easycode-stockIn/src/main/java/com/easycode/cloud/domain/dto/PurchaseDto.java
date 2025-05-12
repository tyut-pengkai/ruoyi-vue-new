package com.easycode.cloud.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购单据处理dto
 * @author hbh
 */
@Alias("PurchaseDto")
public class PurchaseDto extends PurchaseOrderDetail {

    /**
     * 采购凭证类型
     */
    @Excel(name = "采购凭证类型")
    private String billType;

    /**
     * 采购单据类型
     */
    @Excel(name = "采购单据类型")
    private String orderType;

    /**
     * 采购单据状态
     */
    @Excel(name = "采购单据状态")
    private String orderStatus;

    /**
     * 采购公司id
     */
    @Excel(name = "采购公司id")
    private Long companyId;

    /**
     * 采购公司名称
     */
    @Excel(name = "采购公司名称")
    private String companyName;

    /**
     * 采购公司代码
     */
    @Excel(name = "采购公司代码")
    private String companyCode;


    /**
     *计划发货日期
     */
    @Excel(name = "计划发货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliverDate;

    /**
     * 供应商时间
     */
    private Date supplierPeriod;

    /**
     * 供应商id
     */
    @Excel(name = "供应商id")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    private String supplierName;

    /**
     * 供应商代码
     */
    @Excel(name = "供应商代码")
    private String supplierCode;

    /**
     * 采购员
     */
    @Excel(name = "采购员")
    private String buyer;

    /**
     * 送货单
     */
    @Excel(name = "送货单")
    private String deliveryOrderNo;

    /**
     * 是否质检
     */
    @Excel(name = "是否质检")
    private String isQc;

    /**
     * 是否寄售
     */
    @Excel(name = "是否寄售")
    private String isConsign;

    /**
     * 过滤标记 0 否 1是
     */
    private String isFilter;

    /**
     * 采购组描述
     */
    @Excel(name = "采购组描述")
    private String groupName;

    /**
     * 最小包装数
     */
    @Excel(name = "最小包装数")
    private BigDecimal minPacking;

    /**
     * 包装方式
     */
    @Excel(name = "包装方式")
    private String packMethod;
    /**
     * wms 已经制单未收货数量
     */
    private BigDecimal wmsUnreceiveQty;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }

    public String getIsConsign() {
        return isConsign;
    }

    public void setIsConsign(String isConsign) {
        this.isConsign = isConsign;
    }

    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(String isFilter) {
        this.isFilter = isFilter;
    }

    public BigDecimal getMinPacking() {
        return minPacking;
    }

    public void setMinPacking(BigDecimal minPacking) {
        this.minPacking = minPacking;
    }

    public String getPackMethod() {
        return packMethod;
    }

    public void setPackMethod(String packMethod) {
        this.packMethod = packMethod;
    }

    public Date getSupplierPeriod() {
        return supplierPeriod;
    }

    public void setSupplierPeriod(Date supplierPeriod) {
        this.supplierPeriod = supplierPeriod;
    }

    public BigDecimal getWmsUnreceiveQty() {
        return wmsUnreceiveQty;
    }

    public void setWmsUnreceiveQty(BigDecimal wmsUnreceiveQty) {
        this.wmsUnreceiveQty = wmsUnreceiveQty;
    }
}
