package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.annotation.Excel;
import com.easycode.cloud.domain.InspectOrderDetails;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 送检单明细对象 wms_inspect_order_details
 *
 * @author weifu
 * @date 2023-03-29
 */
@Alias("InspectOrderDetailsVo")
public class InspectOrderDetailsVo extends InspectOrderDetails
{

    @Excel(name = "已完成数量")
    private BigDecimal completedQty;

    /** 退货数量 */
    @Excel(name = "本次退货数量")
    private BigDecimal currentReturnQty;

    /** 让步接收数量 */
    @Excel(name = "让步接收数量")
    private BigDecimal currentConcessionQty;

    /** 释放数量 */
    @Excel(name = "释放数量")
    private BigDecimal currentReleaseQty;

    /** 破坏数量 */
    @Excel(name = "破坏数量")
    private BigDecimal currentDestructQty;

    /** 挑选数量 */
    @Excel(name = "挑选数量")
    private BigDecimal currentPickQty;

    /** 项目文本 */
    private String itemText;

    /** 工作分解结构元素 (WBS 元素) */
    private String wbsElem;

    /** 科目分配的网络号 */
    private String netWork;

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getWbsElem() {
        return wbsElem;
    }

    public void setWbsElem(String wbsElem) {
        this.wbsElem = wbsElem;
    }

    public String getNetWork() {
        return netWork;
    }

    public void setNetWork(String netWork) {
        this.netWork = netWork;
    }

    public BigDecimal getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(BigDecimal completedQty) {
        this.completedQty = completedQty;
    }

    public BigDecimal getCurrentReturnQty() {
        return currentReturnQty;
    }

    public void setCurrentReturnQty(BigDecimal currentReturnQty) {
        this.currentReturnQty = currentReturnQty;
    }

    public BigDecimal getCurrentConcessionQty() {
        return currentConcessionQty;
    }

    public void setCurrentConcessionQty(BigDecimal currentConcessionQty) {
        this.currentConcessionQty = currentConcessionQty;
    }

    public BigDecimal getCurrentReleaseQty() {
        return currentReleaseQty;
    }

    public void setCurrentReleaseQty(BigDecimal currentReleaseQty) {
        this.currentReleaseQty = currentReleaseQty;
    }

    public BigDecimal getCurrentDestructQty() {
        return currentDestructQty;
    }

    public void setCurrentDestructQty(BigDecimal currentDestructQty) {
        this.currentDestructQty = currentDestructQty;
    }

    public BigDecimal getCurrentPickQty() {
        return currentPickQty;
    }

    public void setCurrentPickQty(BigDecimal currentPickQty) {
        this.currentPickQty = currentPickQty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("lineNo", getLineNo())
                .append("lot", getLot())
                .append("qcQty", getQcQty())
                .append("destructQty", getDestructQty())
                .append("releaseQty", getReleaseQty())
                .append("furnaceNo", getFurnaceNo())
                .append("prdLot", getPrdLot())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
