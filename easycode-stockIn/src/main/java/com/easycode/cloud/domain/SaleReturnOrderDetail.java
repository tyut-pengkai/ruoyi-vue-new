package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 销售发货退货单明细对象 wms_sale_center_return_order_detail
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("SaleReturnOrderDetail")
public class SaleReturnOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 关联退货单号 */
    private String returnOrderNo;

    /** 行号 */
    private String lineNo;

    /** 物料代码 */
    @Excel(name = "物料代码")
    private String materialNo;

    /** 物料描述 */
    private String materialName;

    /** 旧物料号 */
    private String oldMaterialNo;

    /** 工厂代码 */
    private String factoryCode;

    /** 退货数量 */
    private BigDecimal returnQty;

    /** 单位 */
    private String unit;

    /** 存储地点 */
    private String locationCode;


    /** 操作单位 */
    private String operationUnit;

    /** 操作单位对应数量 */
    @Excel(name = "数量")
    private BigDecimal operationQty;

    /** 批次号 */
    private String lot;

    /**
     * 任务书号
     */
    @Excel(name = "任务书号")
    private String taskBookNo;

    /** 租户号 */
    private Long tenantId;

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
     * 交货项目
     */
    private String pstyv;

    /**
     * 拣配状态
     */
    private String kostk;

    /**
     * 参考单据的单据编号
     */
    private String vgbel;

    /**
     * 参考项目的项目号
     */
    private String vgpos;

    /**
     * 库存类型
     */
    private String insmk;

    public String getPstyv() {
        return pstyv;
    }

    public void setPstyv(String pstyv) {
        this.pstyv = pstyv;
    }

    public String getKostk() {
        return kostk;
    }

    public void setKostk(String kostk) {
        this.kostk = kostk;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getVgpos() {
        return vgpos;
    }

    public void setVgpos(String vgpos) {
        this.vgpos = vgpos;
    }

    public String getInsmk() {
        return insmk;
    }

    public void setInsmk(String insmk) {
        this.insmk = insmk;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setReturnOrderNo(String returnOrderNo)
    {
        this.returnOrderNo = returnOrderNo;
    }

    public String getReturnOrderNo()
    {
        return returnOrderNo;
    }
    public void setLineNo(String lineNo)
    {
        this.lineNo = lineNo;
    }

    public String getLineNo()
    {
        return lineNo;
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
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setReturnQty(BigDecimal returnQty)
    {
        this.returnQty = returnQty;
    }

    public BigDecimal getReturnQty()
    {
        return returnQty;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
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

    public String getTaskBookNo() {
        return taskBookNo;
    }

    public void setTaskBookNo(String taskBookNo) {
        this.taskBookNo = taskBookNo;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("returnOrderNo", getReturnOrderNo())
                .append("lineNo", getLineNo())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("factoryCode", getFactoryCode())
                .append("returnQty", getReturnQty())
                .append("remark", getRemark())
                .append("unit", getUnit())
                .append("operationUnit", getOperationUnit())
                .append("operationQty", getOperationQty())
                .append("tenantId", getTenantId())
                .append("lot", getLot())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
