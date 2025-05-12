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
 * 送检单对象 wms_inspect_order
 *
 * @author weifu
 * @date 2023-03-29
 */
@Alias("InspectOrder")
public class InspectOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 送检单单号 */
    @Excel(name = "检验单号",sort = 2)
    private String orderNo;

    /** 检验单状态 */
    @Excel(name = "检验单状态", readConverterExp = "0=待检验,1=检验完成,2=部分处理,3=已关闭,4=已完成",sort = 12)
    private String billStatus;

    /**
     * 质检数量
     */
    @Excel(name = "检验数量",sort = 5)
    private BigDecimal qty;

    /**
     * 送检数量
     */
    private BigDecimal qcQty;

    /** 检验结果 */
    @Excel(name = "检验结果", readConverterExp = "0=合格,1=不合格",sort = 13)
    private String chkResult;

    /** 物料号 */
    @Excel(name = "物料号",sort = 3)
    private String materialNo;

    /** 物料名称 */
    //@Excel(name = "物料名称")
    private String materialName;

    /** 工厂 */
    //@Excel(name = "工厂")
    private String factoryCode;

    /** 供应商代码 */
    @Excel(name = "供应商代码",sort = 6)
    private String supplierCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称",sort = 7)
    private String supplierName;

    /** 采购单单号 */
    @Excel(name = "采购单/收货单单号",sort = 1)
    private String purchaseOrderNo;

    /** 采购单行号 */
    //@Excel(name = "采购单行号")
    private String purchaseLineNo;

    /** 收货单单号 */
    @Excel(name = "标准入库单号",sort = 8)
    private String receiveOrderNo;

    /** 收货单行号 */
    //@Excel(name = "收货单行号")
    private String receiveLineNo;

    /** 库存地点 */
    @Excel(name = "库存地点",sort = 9)
    private String locationCode;

    /** 收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "收货时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss",sort = 10)
    private Date receiveDate;

    /** 录入结果时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "录入结果时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date enterResultTime;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastDisposeTime;

    /** 检验日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检验日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss",sort = 11)
    private Date qcDate;

    /** 寄售标识 */
    //@Excel(name = "寄售标识", readConverterExp = "0=自有,1=寄售")
    private String isConsign;

    /** 质检员 */
    //@Excel(name = "质检员")
    private String qcInspector;

    /** 租户号 */
    private Long tenantId;

    private String voucherNo;
    /**
     * 是否质检
     */
    private String isQc;

    /**
     * 是否完成上架
     */
    private String isShelf;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setBillStatus(String billStatus)
    {
        this.billStatus = billStatus;
    }

    public String getBillStatus()
    {
        return billStatus;
    }
    public void setChkResult(String chkResult)
    {
        this.chkResult = chkResult;
    }

    public String getChkResult()
    {
        return chkResult;
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
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }

    public String getSupplierCode()
    {
        return supplierCode;
    }
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName()
    {
        return supplierName;
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
    public void setReceiveOrderNo(String receiveOrderNo)
    {
        this.receiveOrderNo = receiveOrderNo;
    }

    public String getReceiveOrderNo()
    {
        return receiveOrderNo;
    }
    public void setReceiveLineNo(String receiveLineNo)
    {
        this.receiveLineNo = receiveLineNo;
    }

    public String getReceiveLineNo()
    {
        return receiveLineNo;
    }
    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getLocationCode()
    {
        return locationCode;
    }
    public void setReceiveDate(Date receiveDate)
    {
        this.receiveDate = receiveDate;
    }

    public Date getReceiveDate()
    {
        return receiveDate;
    }
    public void setIsConsign(String isConsign)
    {
        this.isConsign = isConsign;
    }

    public String getIsConsign()
    {
        return isConsign;
    }
    public void setQcInspector(String qcInspector)
    {
        this.qcInspector = qcInspector;
    }

    public String getQcInspector()
    {
        return qcInspector;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public Date getQcDate() {
        return qcDate;
    }

    public void setQcDate(Date qcDate) {
        this.qcDate = qcDate;
    }

    public Date getEnterResultTime() {
        return enterResultTime;
    }

    public void setEnterResultTime(Date enterResultTime) {
        this.enterResultTime = enterResultTime;
    }

    public Date getLastDisposeTime() {
        return lastDisposeTime;
    }

    public void setLastDisposeTime(Date lastDisposeTime) {
        this.lastDisposeTime = lastDisposeTime;
    }



    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getQcQty() {
        return qcQty;
    }

    public void setQcQty(BigDecimal qcQty) {
        this.qcQty = qcQty;
    }

    public String getIsShelf() {
        return isShelf;
    }

    public void setIsShelf(String isShelf) {
        this.isShelf = isShelf;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("billStatus", getBillStatus())
                .append("chkResult", getChkResult())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("factoryCode", getFactoryCode())
                .append("supplierCode", getSupplierCode())
                .append("supplierName", getSupplierName())
                .append("purchaseOrderNo", getPurchaseOrderNo())
                .append("purchaseLineNo", getPurchaseLineNo())
                .append("receiveOrderNo", getReceiveOrderNo())
                .append("receiveLineNo", getReceiveLineNo())
                .append("locationCode", getLocationCode())
                .append("receiveDate", getReceiveDate())
                .append("isConsign", getIsConsign())
                .append("qcInspector", getQcInspector())
                .append("qcDate", getQcDate())
                .append("enterResultTime", getEnterResultTime())
                .append("lastDisposeTime", getLastDisposeTime())
                .append("voucherNo", getVoucherNo())
                .append("isQc", getIsQc())
                .append("qty", getQty())
                .append("qcQty", getQcQty())
                .append("isShelf", getIsShelf())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
