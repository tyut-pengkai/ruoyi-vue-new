package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 退货任务对象 wms_ret_task
 *
 * @author zhanglei
 * @date 2023-03-13
 */
@Alias("RetTask")
public class RetTask extends BaseEntity {
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
     * 关联明细id
     */
    @Excel(name = "关联明细id")
    private Long detailId;

    /**
     * 任务状态:字典提供
     */
    @Excel(name = "任务状态:字典提供")
    private String taskStatus;

    /**
     * 有效标识
     */
    @Excel(name = "有效标识")
    private String status;

    /**
     * 任务类型
     */
    @Excel(name = "任务类型")
    private String taskType;

    /**
     * 区域代码
     */
    @Excel(name = "区域代码")
    private String areaCode;
    /**
     * 库存地点代码
     */
    @Excel(name = "库存地点代码")
    private String storageLocationCode;

    /**
     * 仓位代码
     */
    @Excel(name = "仓位代码")
    private String positionNo;

    /**
     * 租户号
     */
    @Excel(name = "租户号")
    private Long tenantId;

    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 旧物料号
     */
    private String oldMatrialName;
    /**
     * 入库单号
     */
    private String stockinOrderNo;
    /**
     * 批次号
     */
    private String lot;
    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 完成数量
     */
    private BigDecimal completeQty;

    /**
     * 任务操作数量
     */
    private BigDecimal operationQty;

    /**
     * 完成操作数量
     */
    private BigDecimal operationCompleteQty;

    /**
     * 单位
     */
    private String unit;

    /**
     * 操作单位
     */
    private String operationUnit;


    /**
     * 操作单位对应数量
     */
    private BigDecimal conversDefault;

    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 容器类型
     */
    private String containerType;

    /**
     * 移动类型
     */
    private String moveType;

    /**
     * 打印状态
     * @return
     */
    private String printStatus;

    /**
     * 打印次数
     * @return
     */
    private Integer printSum;

    /**
     *成品中心领料任务号
     */
    private String stockInTaskNo;

    /**
     *仓位Id
     */
    private Long positionId;

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getStockInTaskNo() {
        return stockInTaskNo;
    }

    public void setStockInTaskNo(String stockInTaskNo) {
        this.stockInTaskNo = stockInTaskNo;
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

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getOldMatrialName() {
        return oldMatrialName;
    }

    public void setOldMatrialName(String oldMatrialName) {
        this.oldMatrialName = oldMatrialName;
    }

    public String getStockinOrderNo() {
        return stockinOrderNo;
    }

    public void setStockinOrderNo(String stockinOrderNo) {
        this.stockinOrderNo = stockinOrderNo;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getStorageLocationCode() {
        return storageLocationCode;
    }

    public void setStorageLocationCode(String storageLocationCode) {
        this.storageLocationCode = storageLocationCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
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

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getConversDefault() {
        return conversDefault;
    }

    public void setConversDefault(BigDecimal conversDefault) {
        this.conversDefault = conversDefault;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public Integer getPrintSum() {
        return printSum;
    }

    public void setPrintSum(Integer printSum) {
        this.printSum = printSum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskNo", getTaskNo())
                .append("detailId", getDetailId())
                .append("taskStatus", getTaskStatus())
                .append("status", getStatus())
                .append("taskType", getTaskType())
                .append("areaCode", getAreaCode())
                .append("storageLocationCode", getStorageLocationCode())
                .append("positionNo", getPositionNo())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("printStatus",getPrintStatus())
                .append("printSum",getPrintSum())
                .toString();
    }
}
