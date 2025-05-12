package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.annotation.Excel;
import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.StockInOtherDetail;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 其他入库单dto
 * @author bcp
 */
@Alias("StockInOtherDto")
public class StockInOtherDto extends StockInOther {
    /**
     * 明细集合
     */
    private List<StockInOtherDetail> detailList;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 批次
     */
    private String lot;


    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /**
     * 接受时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public List<StockInOtherDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<StockInOtherDetail> detailList) {
        this.detailList = detailList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
}
