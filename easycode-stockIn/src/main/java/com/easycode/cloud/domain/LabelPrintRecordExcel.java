package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;

/**
 * @Description: 工序委外打印导入
 * @Author XMING
 * @Date 2024-05-24
 */
public class LabelPrintRecordExcel {

    /** 物料描述 */
    @Excel(name = "物料编号")
    private String materialNo;

    /** 总数量 */
    @Excel(name = "总数量")
    private Long totalNum;

    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String vendorCode;

//    /** 批次 */
//    @Excel(name = "批次")
//    private String batchNo;

    /** 最小包装数量 */
    @Excel(name = "最小包装数量")
    private Long minPackageNum;

    /** 采购单号 */
    @Excel(name = "采购单号")
    private String purchaseOrderNo;

    /** 生产订单号/计划号 */
    @Excel(name = "生产订单号/计划号")
    private String productionOrderNo;

    /** 委外工序 */
    @Excel(name = "委外工序",readConverterExp = "0=磷化,1=高频淬火,2=发黑,3=钎焊,4=回斜,5=金车密封端," +
            "6=热处理,7=粗珩,8=绞珩,9=氮化,10=磨槽,11=精磨外圆,12=涂层",combo = "磷化,高频淬火,发黑,钎焊,回斜,金车密封端" +
            ",热处理,粗珩,绞珩,氮化,磨槽,精磨外圆,涂层")
    private String outSourcedProcesses;


    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }


    public Long getMinPackageNum() {
        return minPackageNum;
    }

    public void setMinPackageNum(Long minPackageNum) {
        this.minPackageNum = minPackageNum;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getProductionOrderNo() {
        return productionOrderNo;
    }

    public void setProductionOrderNo(String productionOrderNo) {
        this.productionOrderNo = productionOrderNo;
    }

    public String getOutSourcedProcesses() {
        return outSourcedProcesses;
    }

    public void setOutSourcedProcesses(String outSourcedProcesses) {
        this.outSourcedProcesses = outSourcedProcesses;
    }
}
