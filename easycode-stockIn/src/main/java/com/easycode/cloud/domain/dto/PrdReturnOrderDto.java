package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.PrdReturnOrder;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 生产发料退货tdo
 * @author bcp
 */
@Alias("PrdReturnOrderDto")
public class PrdReturnOrderDto extends PrdReturnOrder {

    private List<PrdReturnOrderDetailDto> detailList;

    private String materialNo;

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

    public List<PrdReturnOrderDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<PrdReturnOrderDetailDto> detailList) {
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
