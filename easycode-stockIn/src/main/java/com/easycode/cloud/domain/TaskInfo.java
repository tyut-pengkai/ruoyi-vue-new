package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 仓管任务对象 wms_task_info
 *
 * @author weifu
 * @date 2022-12-12
 */
@Alias("TaskInfo")
public class TaskInfo extends BaseEntity {
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

    private String[] taskNos;

    /**
     * 任务类型：字典提供
     */
    @Excel(name = "任务类型：字典提供")
    private String taskType;

    /**
     * 任务状态
     */
    @Excel(name = "任务状态")
    private String taskStatus;

    /**
     * 任务状态多选列表
     */
    private String[] taskStatusArr;

    /**
     * 处理人id
     */
    @Excel(name = "处理人id")
    private Long handlerUserId;

    /**
     * 处理人名称
     */
    @Excel(name = "处理人名称")
    private String handlerUserName;

    /**
     * 物料id
     */
    @Excel(name = "物料id")
    private Long materialId;

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
     * 物料描述
     */
    @Excel(name = "物料描述")
    private String materialDesc;

    /**
     * 入库单据号
     */
    @Excel(name = "入库单据号")
    private String stockInOrderNo;

    /**
     * 关联明细id  TODO 成品收货对应的入库任务 关联明细id为成品收货单据的id
     */
    @Excel(name = "关联明细id")
    private Long detailId;

    /**
     * 租户号
     */
    @Excel(name = "租户号")
    private Long tenantId;
    /**
     * 库存地点
     * add by yangyang.zhang 2022-12-12
     */
    @Excel(name="库存地点")
    private String locationCode;

    @Excel(name="源库存地点")
    private String sourceLocationCode;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 完成时间
     */
    @Excel(name="完成时间")
    private Date finishTime;

    /**
     * 停留时长
     */
    @Excel(name="停留时长")
    private Integer stayTimeLength;

    /**
     * 任务标志
     */
    @Excel(name="任务标志")

    private String taskFlag;
    /**
     * 是否已确认
     */
//    @Excel(name="是否已确认")
    private Integer isConfirm;

    /**
     * 打印状态
     */
    private Integer printStatus;

    /**
     * 打印次数
     */
    private Integer printTime;

    /**
     * 打印次数
     * @return
     */
    private Integer printSum;

    /** 仓位号 */
    @Excel(name = "仓位号")
    private String positionNo;

    /** 源仓位号 */
    private String sourcePositionNo;

    /**
     * 看板任务id
     */
    private Long boardTaskId;

    /**
     * 看板任务号
     */
    private String boardTaskNo;

    /**
     * 看板编号
     */
    private String boardCode;

    /**
     *仓位ID
     */
    private Long positionId;

    /** 水位任务号 */
    @Excel(name = "水位任务号")
    private String waterTaskNo;

    /***领用数量(看板变化数量) */
    private BigDecimal quantityLssued;

    /***总容量 */
    private BigDecimal totalCapacity;

    /***源仓位 发货仓位 */
    private String deliverLocation;
    /*** 目的仓位 收货仓位 */
    private String receiveLocation;
    /*** 源库存地点   -- */
    private String addr;

    public String[] getTaskNos() {
        return taskNos;
    }

    public void setTaskNos(String[] taskNos) {
        this.taskNos = taskNos;
    }

    public String getDeliverLocation() {
        return deliverLocation;
    }

    public void setDeliverLocation(String deliverLocation) {
        this.deliverLocation = deliverLocation;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public BigDecimal getQuantityLssued() {
        return quantityLssued;
    }

    public void setQuantityLssued(BigDecimal quantityLssued) {
        this.quantityLssued = quantityLssued;
    }

    public Long getPositionId() {
        return positionId;
    }

    public BigDecimal getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(BigDecimal totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getWaterTaskNo() {
        return waterTaskNo;
    }

    public void setWaterTaskNo(String waterTaskNo) {
        this.waterTaskNo = waterTaskNo;
    }

    /**
     * 移动类型
     */
    private String moveType;

    public TaskInfo() {
    }
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

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setHandlerUserId(Long handlerUserId) {
        this.handlerUserId = handlerUserId;
    }

    public Long getHandlerUserId() {
        return handlerUserId;
    }

    public void setHandlerUserName(String handlerUserName) {
        this.handlerUserName = handlerUserName;
    }

    public String getHandlerUserName() {
        return handlerUserName;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setStockInOrderNo(String stockInOrderNo) {
        this.stockInOrderNo = stockInOrderNo;
    }

    public String getStockInOrderNo() {
        return stockInOrderNo;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String[] getTaskStatusArr() {
        return taskStatusArr;
    }

    public void setTaskStatusArr(String[] taskStatusArr) {
        this.taskStatusArr = taskStatusArr;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getSourceLocationCode() {
        return sourceLocationCode;
    }

    public void setSourceLocationCode(String sourceLocationCode) {
        this.sourceLocationCode = sourceLocationCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getStayTimeLength() {
        return stayTimeLength;
    }

    public void setStayTimeLength(Integer stayTimeLength) {
        this.stayTimeLength = stayTimeLength;
    }

    public String getTaskFlag() {
        return taskFlag;
    }

    public void setTaskFlag(String taskFlag) {
        this.taskFlag = taskFlag;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }
    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    public Integer getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Integer printTime) {
        this.printTime = printTime;
    }

    public Integer getPrintSum() {
        return printSum;
    }

    public void setPrintSum(Integer printSum) {
        this.printSum = printSum;
    }

    public Long getBoardTaskId() {
        return boardTaskId;
    }

    public void setBoardTaskId(Long boardTaskId) {
        this.boardTaskId = boardTaskId;
    }

    public String getBoardTaskNo() {
        return boardTaskNo;
    }

    public void setBoardTaskNo(String boardTaskNo) {
        this.boardTaskNo = boardTaskNo;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getSourcePositionNo() {
        return sourcePositionNo;
    }

    public void setSourcePositionNo(String sourcePositionNo) {
        this.sourcePositionNo = sourcePositionNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskNo", getTaskNo())
                .append("taskType", getTaskType())
                .append("taskStatus", getTaskStatus())
                .append("handlerUserId", getHandlerUserId())
                .append("handlerUserName", getHandlerUserName())
                .append("materialId", getMaterialId())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("materialDesc", getMaterialDesc())
                .append("stockInOrderNo", getStockInOrderNo())
                .append("detailId", getDetailId())
                .append("tenantId", getTenantId())
                .append("locationCode", getLocationCode())
                .append("sourceLocationCode", getSourceLocationCode())
                .append("finishTime", getFinishTime())
                .append("stayTimeLength", getStayTimeLength())
                .append("taskFlag", getTaskFlag())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
