package com.easycode.cloud.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 采购单临时-明细对象 wms_purchase_order_detail_raw
 * 
 * @author weifu
 * @date 2023-02-20
 */
@Alias("WmsPurchaseOrderDetailRaw")
public class WmsPurchaseOrderDetailRaw extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 工厂id */
    @Excel(name = "工厂id")
    private Long factoryId;

    /** 工厂名称 */
    @Excel(name = "工厂名称")
    private String factoryName;

    /** 工厂代码 */
    @Excel(name = "工厂代码")
    private String factoryCode;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 物料代码 */
    @Excel(name = "物料代码")
    private String materialNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 交货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 采购单据id */
    @Excel(name = "采购单据id")
    private Long purchaseOrderId;

    /** 采购单号 */
    @Excel(name = "采购单号")
    private String purchaseOrderNo;

    /** 采购单行号 */
    @Excel(name = "采购单行号")
    private String purchaseLineNo;

    /** 采购退货单号 */
    @Excel(name = "采购退货单号")
    private String purchaseRefundOrderNo;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 总需求数量; */
    @Excel(name = "总需求数量;")
    private BigDecimal totalPlanQty;

    /** 已制间数量 */
    @Excel(name = "已制间数量")
    private BigDecimal madeQty;

    /** 需求数量 */
    @Excel(name = "需求数量")
    private BigDecimal planQty;

    /** 库存地点 */
    @Excel(name = "库存地点")
    private String stoLocation;

    /** 是否免检 */
    @Excel(name = "是否免检")
    private String isExempted;

    /** 是否寄售 */
    @Excel(name = "是否寄售")
    private String isConsigned;

    /** 不允许交货 */
    @Excel(name = "不允许交货")
    private String noDeliveryFlag;

    /** 批次 */
    @Excel(name = "批次")
    private String batchNo;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** sap已完成数量 */
    @Excel(name = "sap已完成数量")
    private BigDecimal completeQty;

    /** 物料组 */
    @Excel(name = "物料组")
    private String materialGroup;

    /** 物料组描述 */
    @Excel(name = "物料组描述")
    private String materialGroupDesc;

    /**
     * 移动类型
     */
    private String moveType;
    /**
     * 拣配状态
     */
    private String pickStatus;

    public String getMaterialGroup() {
        return materialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }

    public String getMaterialGroupDesc() {
        return materialGroupDesc;
    }

    public void setMaterialGroupDesc(String materialGroupDesc) {
        this.materialGroupDesc = materialGroupDesc;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public String getLoekz() {
        return loekz;
    }

    public void setLoekz(String loekz) {
        this.loekz = loekz;
    }

    public String getExcFinFlag() {
        return excFinFlag;
    }

    public void setExcFinFlag(String excFinFlag) {
        this.excFinFlag = excFinFlag;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getRefundItem() {
        return refundItem;
    }

    public void setRefundItem(String refundItem) {
        this.refundItem = refundItem;
    }

    public String getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(String processFlag) {
        this.processFlag = processFlag;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getDeliveryLineNo() {
        return deliveryLineNo;
    }

    public void setDeliveryLineNo(String deliveryLineNo) {
        this.deliveryLineNo = deliveryLineNo;
    }

    /** 资料类别删除标识 */
    @Excel(name = "资料类别删除标识")
    private String loekz;

    /** 交换完成标识 */
    @Excel(name = "交换完成标识")
    private String excFinFlag;

    /** 库存类型 */
    @Excel(name = "库存类型")
    private String storageType;

    /** 退货项目 */
    @Excel(name = "退货项目")
    private String refundItem;

    /** 是否处理标识 */
    @Excel(name = "是否处理标识")
    private String processFlag;

    /** 采购凭证中的项目类别 */
    @Excel(name = "采购凭证中的项目类别")
    private String itemType;

    /** 处理时间 */
    @Excel(name = "处理时间")
    private Date processTime;

    /** 交货计划行计数器 */
    @Excel(name = "交货计划行计数器")
    private String deliveryLineNo;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFactoryId(Long factoryId) 
    {
        this.factoryId = factoryId;
    }

    public Long getFactoryId() 
    {
        return factoryId;
    }
    public void setFactoryName(String factoryName) 
    {
        this.factoryName = factoryName;
    }

    public String getFactoryName() 
    {
        return factoryName;
    }
    public void setFactoryCode(String factoryCode) 
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode() 
    {
        return factoryCode;
    }
    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }
    public void setMaterialNo(String materialNo) 
    {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() 
    {
        return materialNo;
    }
    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }
    public void setOldMaterialNo(String oldMaterialNo) 
    {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() 
    {
        return oldMaterialNo;
    }
    public void setDeliveryDate(Date deliveryDate) 
    {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryDate() 
    {
        return deliveryDate;
    }
    public void setPurchaseOrderId(Long purchaseOrderId) 
    {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getPurchaseOrderId() 
    {
        return purchaseOrderId;
    }
    public void setPurchaseOrderNo(String purchaseOrderNo) 
    {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseOrderNo() 
    {
        return purchaseOrderNo;
    }
    public void setPurchaseLineNo(String purchaseLineNo)
    {
        this.purchaseLineNo = purchaseLineNo;
    }

    public String getPurchaseLineNo()
    {
        return purchaseLineNo;
    }
    public void setPurchaseRefundOrderNo(String purchaseRefundOrderNo) 
    {
        this.purchaseRefundOrderNo = purchaseRefundOrderNo;
    }

    public String getPurchaseRefundOrderNo() 
    {
        return purchaseRefundOrderNo;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setTotalPlanQty(BigDecimal totalPlanQty) 
    {
        this.totalPlanQty = totalPlanQty;
    }

    public BigDecimal getTotalPlanQty() 
    {
        return totalPlanQty;
    }
    public void setMadeQty(BigDecimal madeQty) 
    {
        this.madeQty = madeQty;
    }

    public BigDecimal getMadeQty() 
    {
        return madeQty;
    }
    public void setPlanQty(BigDecimal planQty) 
    {
        this.planQty = planQty;
    }

    public BigDecimal getPlanQty() 
    {
        return planQty;
    }
    public void setStoLocation(String stoLocation) 
    {
        this.stoLocation = stoLocation;
    }

    public String getStoLocation() 
    {
        return stoLocation;
    }
    public void setIsExempted(String isExempted) 
    {
        this.isExempted = isExempted;
    }

    public String getIsExempted() 
    {
        return isExempted;
    }
    public void setIsConsigned(String isConsigned) 
    {
        this.isConsigned = isConsigned;
    }

    public String getIsConsigned() 
    {
        return isConsigned;
    }
    public void setNoDeliveryFlag(String noDeliveryFlag) 
    {
        this.noDeliveryFlag = noDeliveryFlag;
    }

    public String getNoDeliveryFlag() 
    {
        return noDeliveryFlag;
    }
    public void setBatchNo(String batchNo) 
    {
        this.batchNo = batchNo;
    }

    public String getBatchNo() 
    {
        return batchNo;
    }
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(String pickStatus) {
        this.pickStatus = pickStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("factoryId", getFactoryId())
            .append("factoryName", getFactoryName())
            .append("factoryCode", getFactoryCode())
            .append("materialId", getMaterialId())
            .append("materialNo", getMaterialNo())
            .append("materialName", getMaterialName())
            .append("oldMaterialNo", getOldMaterialNo())
            .append("deliveryDate", getDeliveryDate())
            .append("purchaseOrderId", getPurchaseOrderId())
            .append("purchaseOrderNo", getPurchaseOrderNo())
            .append("purchaseLineNo", getPurchaseLineNo())
            .append("purchaseRefundOrderNo", getPurchaseRefundOrderNo())
            .append("unit", getUnit())
            .append("totalPlanQty", getTotalPlanQty())
            .append("madeQty", getMadeQty())
            .append("planQty", getPlanQty())
            .append("stoLocation", getStoLocation())
            .append("isExempted", getIsExempted())
            .append("isConsigned", getIsConsigned())
            .append("noDeliveryFlag", getNoDeliveryFlag())
            .append("batchNo", getBatchNo())
            .append("completeQty", getCompleteQty())
            .append("moveType", getMoveType())
            .append("pickStatus", getPickStatus())
            .append("materialGroup", getMaterialGroup())
            .append("materialGroupDesc", getMaterialGroupDesc())
            .append("loekz", getLoekz())
            .append("excFinFlag", getExcFinFlag())
            .append("storageType", getStorageType())
            .append("refundItem", getRefundItem())
            .append("processFlag", getProcessFlag())
            .append("itemType", getItemType())
            .append("processTime", getProcessTime())
            .append("deliveryLineNo", getDeliveryLineNo())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
