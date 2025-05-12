package com.easycode.cloud.domain.dto;


import com.easycode.cloud.domain.LotDto;
import com.easycode.cloud.domain.TaskInfo;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 仓管任务对象 入参dto
 *
 * @author bcp
 */
@Alias("TaskInfoDto")
public class TaskInfoDto extends TaskInfo {

    /**
     * 收货数量
     */
    private BigDecimal receivedQty;
    /**
     * 收货数量
     */
    private BigDecimal operationReceivedQty;

    /**
     * 操作单位对应操作数量
     */
    private BigDecimal operationQty;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 库存地点
     */
    private String location;

    /**
     * 采购单号
     */
    private String purchaseOrderNo;

    /**
     * 采购单行号
     */
    private String purchaseLineNo;

    /**
     * 批次号
     */
    private String lotNo;

    /**
     * 是否质检
     */
    private String isQc;

    /**
     * 是否寄售
     */
    private String isConsign;

    /**
     * 货主id
     */
    private Long factoryId;

    /**
     * 货主
     */
    private String factoryCode;

    /**
     * 货主名称
     */
    private String factoryName;

    /**
     * 货主名称
     */
    private String lineNo;

    /**
     * 供应商id
     */
    private Long vendorId;

    /**
     * 供应商code
     */
    private String vendorCode;

    /**
     * 供应商名称
     */
    private String vendorName;

    /**
     * 默认单位
     */
    private String unit;

    /**
     * 默认单位
     */
    private String operationUnit;

    /**
     * 生产批次/sap默认批次
     */
    private String productionLot;

    /**
     * 送货数量
     */
    private BigDecimal deliverQty;

    /**
     * 换算数值
     */
    private BigDecimal conversDefault;

    /**
     * 标准入口单id
     */
    private Long stockInOrderId;

    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 仓位
     */
    private String positionNo;

    /**
     * 标签
     */
    private String label;

    /**
     * 批次集合
     */
    private List<LotDto> lotNoNumList;

    /**
     * 任务状态集合
     */
    private List<String> statusList;

    /**
     * 任务状态集合
     */
    private List<String> orderStatusList;

    /**
     * 箱内总数
     */
    /**
     * 送货数量
     */
    private BigDecimal totalQty;

    /**
     * 源库存地点
     */
    private String sourceLocation;
    /**
     * 生产订单号
     */
    private String prdOrderNo;

    /**
     * 成品收货详情状态
     */
    private String detailStatus;

    /**
     * 半成品收货状态
     */
    private String orderStatus;

    /**
     * ASN号
     */
    private String asnNo;

    /**
     * 进入详情页面的时间
     */
    private String startTime;

    /**
     * 时长时间
     */
    private Date productTime;

    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 送货要求内容
     */
    private String requirementContent;

    private String werks;

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseLineNo() {
        return purchaseLineNo;
    }

    public void setPurchaseLineNo(String purchaseLineNo) {
        this.purchaseLineNo = purchaseLineNo;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getDeliverQty() {
        return deliverQty;
    }

    public void setDeliverQty(BigDecimal deliverQty) {
        this.deliverQty = deliverQty;
    }

    public Long getStockInOrderId() {
        return stockInOrderId;
    }

    public void setStockInOrderId(Long stockInOrderId) {
        this.stockInOrderId = stockInOrderId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public List<LotDto> getLotNoNumList() {
        return lotNoNumList;
    }

    public void setLotNoNumList(List<LotDto> lotNoNumList) {
        this.lotNoNumList = lotNoNumList;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getOperationQty() {
        return operationQty;
    }

    public void setOperationQty(BigDecimal operationQty) {
        this.operationQty = operationQty;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getOperationReceivedQty() {
        return operationReceivedQty;
    }

    public void setOperationReceivedQty(BigDecimal operationReceivedQty) {
        this.operationReceivedQty = operationReceivedQty;
    }

    public String getPrdOrderNo() {
        return prdOrderNo;
    }

    public void setPrdOrderNo(String prdOrderNo) {
        this.prdOrderNo = prdOrderNo;
    }

    public String getRequirementContent() {
        return requirementContent;
    }

    public void setRequirementContent(String requirementContent) {
        this.requirementContent = requirementContent;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<String> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<String> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public BigDecimal getConversDefault() {
        return conversDefault;
    }

    public void setConversDefault(BigDecimal conversDefault) {
        this.conversDefault = conversDefault;
    }

    public String getProductionLot() {
        return productionLot;
    }

    public void setProductionLot(String productionLot) {
        this.productionLot = productionLot;
    }

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Date getProductTime() {
        return productTime;
    }

    public void setProductTime(Date productTime) {
        this.productTime = productTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
