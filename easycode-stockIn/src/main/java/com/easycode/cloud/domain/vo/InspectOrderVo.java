package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.annotation.Excel;
import com.easycode.cloud.domain.InspectOrder;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 送检单对象 wms_inspect_order
 *
 * @author weifu
 * @date 2023-03-29
 */
@Alias("InspectOrderVo")
public class InspectOrderVo extends InspectOrder
{
    private static final long serialVersionUID = 1L;

    private String startTime;

    private String endTime;

    /**
     * 高亮类型
     */
    private String highlight;

    /**
     * 质检数量
     */
    @Excel(name = "质检数量",sort = 4)
    private BigDecimal qcQty;

    /**
     * 仓位信息
     */
    private String positionNo;

    /**
     * 批次信息
     */
    private String lot;
    /**
     * 任务id
    * */
    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

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

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public BigDecimal getQcQty() {
        return qcQty;
    }

    public void setQcQty(BigDecimal qcQty) {
        this.qcQty = qcQty;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
}
