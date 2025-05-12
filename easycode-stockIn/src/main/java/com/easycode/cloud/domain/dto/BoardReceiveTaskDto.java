package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.constant.CommonYesOrNo;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;
@Alias("BoardReceiveTaskDto")
public class BoardReceiveTaskDto {
    //主键
    private Long id;

    //任务号
    @Excel(name = "任务号")
    private String taskNo;
    //任务类型
    private String taskType;
    //看板代码
    @Excel(name = "看板号/水位号")
    private String boardCode;
    //物料号
    @Excel(name = "物料号")
    private String materialNo;
    //物料名称
    @Excel(name = "物料名称")
    private String materialName;
    //旧物料号
    @Excel(name = "旧物料号")
    private String oldMaterialNo;
    //批次
    @Excel(name = "批次")
    private String batchNo;
    //是否已确认
    private int isConfirm;
    //源仓位
    private String defaultDeliverPosition;
    //目的仓位
    private String defaultReceivePosition;
    //生成时间
    @Excel(name = "创建时间")
    private String createTime;
    //领用时间
    private Date stayTimeLength;
    //是否翻包
    @Excel(name = "是否翻包",readConverterExp = "1=是,0=否")
    private String isTurn;
    //翻包区
    @Excel(name = "翻包区")
    private String turnArea;
    //任务状态
    @Excel(name = "任务状态",readConverterExp = "1=新建,2=在途,3=完成,4=关闭")
    private String taskStatus;

    //看板容量
    private String capacity;

    //默认发货库存地点
    @Excel(name = "发货库存地点")
    private String defaultDeliverLocation;

    //默认收货库存地点
    @Excel(name = "收货库存地点")
    private String defaultReceiveLocation;
    //实际数量
    @Excel(name = "数量")
    private BigDecimal quantityLssued;
    //翻包任务号
    private String waterTaskNo;

    //入库批次
    private String stockInLot;
    //可用数量（实际数量）
    private String availableQty;

    // 关联翻包任务号
    private String relTaskNo;
    /**
     * 源库存地点
     */
    private String sourceLocationCode;
    /**
     * 源仓位
     */
    @Excel(name = "发货仓位")
    private String  sourcePositionNo;
    /**
     * 目的仓位
     */
    @Excel(name = "收货仓位")
    private String  positionNo;
    //
    private String boardTaskNo;
    @Excel(name = "打印次数")
    private Integer printSum;
    @Excel(name = "打印状态",readConverterExp = "1=已打印,0=未打印")
    private String printStatus;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public Integer getPrintSum() {
        return printSum;
    }

    public void setPrintSum(Integer printSum) {
        this.printSum = printSum;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getBoardTaskNo() {
        return boardTaskNo;
    }

    public void setBoardTaskNo(String boardTaskNo) {
        this.boardTaskNo = boardTaskNo;
    }

    public String getSourceLocationCode() {
        return sourceLocationCode;
    }

    public void setSourceLocationCode(String sourceLocationCode) {
        this.sourceLocationCode = sourceLocationCode;
    }

    public String getSourcePositionNo() {
        return sourcePositionNo;
    }

    public void setSourcePositionNo(String sourcePositionNo) {
        this.sourcePositionNo = sourcePositionNo;
    }

    public String getStockInLot() {
        return stockInLot;
    }

    public void setStockInLot(String stockInLot) {
        this.stockInLot = stockInLot;
    }

    public String getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(String availableQty) {
        this.availableQty = availableQty;
    }

    public String getWaterTaskNo() {
        return waterTaskNo;
    }

    public void setWaterTaskNo(String waterTaskNo) {
        this.waterTaskNo = waterTaskNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(int isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getDefaultDeliverPosition() {
        return defaultDeliverPosition;
    }

    public void setDefaultDeliverPosition(String defaultDeliverPosition) {
        this.defaultDeliverPosition = defaultDeliverPosition;
    }

    public String getDefaultReceivePosition() {
        return defaultReceivePosition;
    }

    public void setDefaultReceivePosition(String defaultReceivePosition) {
        this.defaultReceivePosition = defaultReceivePosition;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Date getStayTimeLength() {
        return stayTimeLength;
    }

    public void setStayTimeLength(Date stayTimeLength) {
        this.stayTimeLength = stayTimeLength;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDefaultDeliverLocation() {
        return defaultDeliverLocation;
    }

    public void setDefaultDeliverLocation(String defaultDeliverLocation) {
        this.defaultDeliverLocation = defaultDeliverLocation;
    }

    public String getDefaultReceiveLocation() {
        return defaultReceiveLocation;
    }

    public void setDefaultReceiveLocation(String defaultReceiveLocation) {
        this.defaultReceiveLocation = defaultReceiveLocation;
    }

    public String getTurnArea() {
        return turnArea;
    }

    public void setTurnArea(String turnArea) {
        this.turnArea = turnArea;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    public String getIsTurn() {
        return isTurn;
    }

    public void setIsTurn(String isTurn) {
        this.isTurn = isTurn;
    }

    public BigDecimal getQuantityLssued() {
        return quantityLssued;
    }

    public void setQuantityLssued(BigDecimal quantityLssued) {
        this.quantityLssued = quantityLssued;
    }

    public String getRelTaskNo() {
        return relTaskNo;
    }

    public void setRelTaskNo(String relTaskNo) {
        this.relTaskNo = relTaskNo;
    }
}
