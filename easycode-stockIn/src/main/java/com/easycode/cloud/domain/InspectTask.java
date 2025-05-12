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
 * 送检任务对象 wms_shelf_task
 *
 * @author zhanglei
 * @date 2023-04-12
 */
@Alias("InspectTask")
public class InspectTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务号
     */
    @Excel(name = "任务号")
    private String taskNo;

    /**
     * 任务类型
     */
    @Excel(name = "任务类型")
    private String taskType;

    /**
     * 任务状态
     */
    @Excel(name = "任务状态")
    private String status;

    /**
     * 任务状态多选列表
     */
    private String[] taskStatusArr;

    /**
     * 分配时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "分配时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date allocateTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 物料号
     */
    @Excel(name = "物料号")
    private String materialNo;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 旧物料号
     */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /**
     * 入库单号
     */
    @Excel(name = "入库单号")
    private String stockinOrderNo;

    /**
     * 入库单行号
     */
    @Excel(name = "入库单行号")
    private String stockinLineNo;

    /**
     * 质检单号
     */
    @Excel(name = "质检单号")
    private String inspectOrderNo;

    /**
     * 上架库存地点
     */
    @Excel(name = "上架库存地点")
    private String locationCode;
    /**
     * 源库存地点
     */
    private String sourceLocationCode;
    /**
     * 上架区域
     */
    @Excel(name = "上架区域")
    private String areaCode;

    /**
     * 源区域
     */
    private String sourceAreaCode;

    /**
     * 上架仓位
     */
    @Excel(name = "上架仓位")
    private String positionNo;
    /**
     * 源仓位
     */
    private String sourcePositionNo;

    /**
     * 默认单位
     */
    @Excel(name = "默认单位")
    private String unit;

    /**
     * 操作单位
     */
    @Excel(name = "操作单位")
    private String operationUnit;

    /**
     * 任务操作数量
     */
    @Excel(name = "任务操作数量")
    private BigDecimal operationQty;

    /**
     * 任务数量
     */
    @Excel(name = "任务数量")
    private BigDecimal qty;

    /**
     * 操作完成数量
     */
    @Excel(name = "操作完成数量")
    private BigDecimal operationCompleteQty;

    /**
     * 完成数量
     */
    @Excel(name = "完成数量")
    private BigDecimal completeQty;

    /**
     * 批次号
     */
    private String lot;
    /**
     * 库存台账id
     */
    private Long inventoryId;

    /**
     * 租户号
     */
    @Excel(name = "租户号")
    private Long tenantId;

    /**
     * 箱号
     */
    private String containerNo;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAllocateTime(Date allocateTime) {
        this.allocateTime = allocateTime;
    }

    public Date getAllocateTime() {
        return allocateTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setStockinOrderNo(String stockinOrderNo) {
        this.stockinOrderNo = stockinOrderNo;
    }

    public String getStockinOrderNo() {
        return stockinOrderNo;
    }

    public void setStockinLineNo(String stockinLineNo) {
        this.stockinLineNo = stockinLineNo;
    }

    public String getStockinLineNo() {
        return stockinLineNo;
    }

    public void setInspectOrderNo(String inspectOrderNo) {
        this.inspectOrderNo = inspectOrderNo;
    }

    public String getInspectOrderNo() {
        return inspectOrderNo;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getSourceLocationCode() {
        return sourceLocationCode;
    }

    public void setSourceLocationCode(String sourceLocationCode) {
        this.sourceLocationCode = sourceLocationCode;
    }

    public String getSourceAreaCode() {
        return sourceAreaCode;
    }

    public void setSourceAreaCode(String sourceAreaCode) {
        this.sourceAreaCode = sourceAreaCode;
    }

    public String getSourcePositionNo() {
        return sourcePositionNo;
    }

    public void setSourcePositionNo(String sourcePositionNo) {
        this.sourcePositionNo = sourcePositionNo;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getOperationQty() {
        return operationQty;
    }

    public void setOperationQty(BigDecimal operationQty) {
        this.operationQty = operationQty;
    }

    public BigDecimal getOperationCompleteQty() {
        return operationCompleteQty;
    }

    public void setOperationCompleteQty(BigDecimal operationCompleteQty) {
        this.operationCompleteQty = operationCompleteQty;
    }

    public String[] getTaskStatusArr() {
        return taskStatusArr;
    }

    public void setTaskStatusArr(String[] taskStatusArr) {
        this.taskStatusArr = taskStatusArr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskNo", getTaskNo())
                .append("taskType", getTaskType())
                .append("status", getStatus())
                .append("allocateTime", getAllocateTime())
                .append("finishTime", getFinishTime())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("stockinOrderNo", getStockinOrderNo())
                .append("stockinLineNo", getStockinLineNo())
                .append("inspectOrderNo", getInspectOrderNo())
                .append("locationCode", getLocationCode())
                .append("sourceLocationCode", getSourceLocationCode())
                .append("areaCode", getAreaCode())
                .append("sourceAreaCode", getSourceAreaCode())
                .append("positionNo", getPositionNo())
                .append("sourcePositionNo", getSourcePositionNo())
                .append("qty", getQty())
                .append("unit", getUnit())
                .append("operationUnit", getOperationUnit())
                .append("operationQty", getOperationQty())
                .append("operationCompleteQty", getOperationCompleteQty())
                .append("completeQty", getCompleteQty())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
