package com.easycode.cloud.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 标签打印记录对象 wms_label_print_record
 * 
 * @author weifu
 * @date 2024-05-14
 */
@Alias("LabelPrintRecord")
public class LabelPrintRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id主键 */
    private Long id;

    /** 物料代码 */
    @Excel(name = "物料代码")
    private String materialNo;

    /** 标签类型 */
    @Excel(name = "标签类型")
    private Long labelType;

    /** 物料描述 */
    @Excel(name = "物料描述")
    private String materialDesc;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 批次 */
    @Excel(name = "批次")
    private String batchNo;

    /** 箱号 */
    @Excel(name = "箱号")
    private String containerNo;

    /** 生产订单号/计划号 */
    @Excel(name = "生产订单号/计划号")
    private String productionOrderNo;

    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String vendorCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String vendorName;

    /** 最小单位 */
    @Excel(name = "最小单位")
    private String leastUnit;

    /** 打印数量 */
    @Excel(name = "打印数量")
    private Long printNum;

    /** 打印时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "打印时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date printTime;

    /** 操作人/打印人 */
    @Excel(name = "操作人/打印人")
    private String operator;

    /** 工厂代码 */
    @Excel(name = "工厂代码")
    private String plantCode;

    /** 租户id */
    @Excel(name = "租户id")
    private Long tenantId;

    /** 总数量 */
    @Excel(name = "总数量")
    private Long totalNum;

    /** 最小包装数量 */
    @Excel(name = "最小包装数量")
    private Long minPackageNum;

    /** 首箱数量 */
    @Excel(name = "首箱数量")
    private Long firstBoxNum;

    /** 采购单号 */
    @Excel(name = "采购单号")
    private String purchaseOrderNo;

    /** 采购单行号 */
    @Excel(name = "采购单行号")
    private String purchaseLineNo;

    /** 送货单号 */
    @Excel(name = "送货单号")
    private String deliveryOrderNo;

    /** 送货单行号 */
    @Excel(name = "送货单行号")
    private String deliveryLineNo;

    /** 委外工序 */
    @Excel(name = "委外工序")
    private String outSourcedProcesses;

    /** 流水号 */
    private Integer serialNumber;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialNo(String materialNo) 
    {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() 
    {
        return materialNo;
    }
    public void setLabelType(Long labelType) 
    {
        this.labelType = labelType;
    }

    public Long getLabelType() 
    {
        return labelType;
    }
    public void setMaterialDesc(String materialDesc) 
    {
        this.materialDesc = materialDesc;
    }

    public String getMaterialDesc() 
    {
        return materialDesc;
    }
    public void setOldMaterialNo(String oldMaterialNo) 
    {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() 
    {
        return oldMaterialNo;
    }
    public void setBatchNo(String batchNo) 
    {
        this.batchNo = batchNo;
    }

    public String getBatchNo() 
    {
        return batchNo;
    }
    public void setContainerNo(String containerNo) 
    {
        this.containerNo = containerNo;
    }

    public String getContainerNo() 
    {
        return containerNo;
    }
    public void setProductionOrderNo(String productionOrderNo) 
    {
        this.productionOrderNo = productionOrderNo;
    }

    public String getProductionOrderNo() 
    {
        return productionOrderNo;
    }
    public void setVendorCode(String vendorCode) 
    {
        this.vendorCode = vendorCode;
    }

    public String getVendorCode() 
    {
        return vendorCode;
    }
    public void setVendorName(String vendorName) 
    {
        this.vendorName = vendorName;
    }

    public String getVendorName() 
    {
        return vendorName;
    }
    public void setLeastUnit(String leastUnit) 
    {
        this.leastUnit = leastUnit;
    }

    public String getLeastUnit() 
    {
        return leastUnit;
    }
    public void setPrintNum(Long printNum) 
    {
        this.printNum = printNum;
    }

    public Long getPrintNum() 
    {
        return printNum;
    }
    public void setPrintTime(Date printTime) 
    {
        this.printTime = printTime;
    }

    public Date getPrintTime() 
    {
        return printTime;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setPlantCode(String plantCode) 
    {
        this.plantCode = plantCode;
    }

    public String getPlantCode() 
    {
        return plantCode;
    }
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }
    public void setTotalNum(Long totalNum) 
    {
        this.totalNum = totalNum;
    }

    public Long getTotalNum() 
    {
        return totalNum;
    }
    public void setMinPackageNum(Long minPackageNum) 
    {
        this.minPackageNum = minPackageNum;
    }

    public Long getMinPackageNum() 
    {
        return minPackageNum;
    }
    public void setFirstBoxNum(Long firstBoxNum) 
    {
        this.firstBoxNum = firstBoxNum;
    }

    public Long getFirstBoxNum() 
    {
        return firstBoxNum;
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
    public void setDeliveryOrderNo(String deliveryOrderNo) 
    {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public String getDeliveryOrderNo() 
    {
        return deliveryOrderNo;
    }
    public void setDeliveryLineNo(String deliveryLineNo) 
    {
        this.deliveryLineNo = deliveryLineNo;
    }

    public String getDeliveryLineNo() 
    {
        return deliveryLineNo;
    }
    public void setOutSourcedProcesses(String outSourcedProcesses) 
    {
        this.outSourcedProcesses = outSourcedProcesses;
    }

    public String getOutSourcedProcesses() 
    {
        return outSourcedProcesses;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }


    @Override
    public String toString() {
        return "LabelPrintRecord{" +
                "id=" + id +
                ", materialNo='" + materialNo + '\'' +
                ", labelType=" + labelType +
                ", materialDesc='" + materialDesc + '\'' +
                ", oldMaterialNo='" + oldMaterialNo + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", containerNo='" + containerNo + '\'' +
                ", productionOrderNo='" + productionOrderNo + '\'' +
                ", vendorCode='" + vendorCode + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", leastUnit='" + leastUnit + '\'' +
                ", printNum=" + printNum +
                ", printTime=" + printTime +
                ", operator='" + operator + '\'' +
                ", plantCode='" + plantCode + '\'' +
                ", tenantId=" + tenantId +
                ", totalNum=" + totalNum +
                ", minPackageNum=" + minPackageNum +
                ", firstBoxNum=" + firstBoxNum +
                ", purchaseOrderNo='" + purchaseOrderNo + '\'' +
                ", purchaseLineNo='" + purchaseLineNo + '\'' +
                ", deliveryOrderNo='" + deliveryOrderNo + '\'' +
                ", deliveryLineNo='" + deliveryLineNo + '\'' +
                ", outSourcedProcesses='" + outSourcedProcesses + '\'' +
                ", serialNumber=" + serialNumber +
                '}';
    }
}
