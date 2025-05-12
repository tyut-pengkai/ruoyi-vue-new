package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.LabelPrintRecord;
import org.apache.ibatis.type.Alias;

/**
 * 标签打印记录查询对象
 *
 * @author Administrator
 */
@Alias("LabelPrintRecordVo")
public class LabelPrintRecordVo extends LabelPrintRecord {

//    /**
//     * 物料代码
//     */
//    private String materialNo;
//
//    /**
//     * 标签类型
//     */
//    private Long labelType;
//
//    /**
//     * 批次
//     */
//    private String batchNo;
//
//    /**
//     * 箱号
//     */
//    private String containerNo;
//
//    /**
//     * 生产订单号/计划号
//     */
//    private String productionOrderNo;
//
//    /**
//     * 供应商名称
//     */
//    private String vendorName;

    /**
     * 打印时间
     */
    private String startPrintTime;

    private String endPrintTime;

    public String getStartPrintTime() {
        return startPrintTime;
    }

    public void setStartPrintTime(String startPrintTime) {
        this.startPrintTime = startPrintTime;
    }

    public String getEndPrintTime() {
        return endPrintTime;
    }

    public void setEndPrintTime(String endPrintTime) {
        this.endPrintTime = endPrintTime;
    }
}