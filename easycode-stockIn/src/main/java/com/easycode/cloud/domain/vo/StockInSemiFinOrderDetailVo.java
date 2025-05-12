package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import org.apache.ibatis.type.Alias;

/**
 * 半成品收货vo
 * @author bcp
 */
@Alias("StockInSemiFinOrderDetailVo")
public class StockInSemiFinOrderDetailVo extends StockInSemiFinOrderDetail {
    /**
     * 半成品收货单号
     */
    private String orderNo;

    /**
     * 半成品明细单据状态集合
     */
    private String[] detailStatusArr;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String[] getDetailStatusArr() {
        return detailStatusArr;
    }

    public void setDetailStatusArr(String[] detailStatusArr) {
        this.detailStatusArr = detailStatusArr;
    }
}
