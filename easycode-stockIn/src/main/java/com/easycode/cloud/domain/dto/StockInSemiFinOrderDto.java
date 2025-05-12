package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.StockInSemiFinOrder;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 半成品入库dto
 * @author 15873
 */
@Alias("StockInSemiFinOrderDto")
public class StockInSemiFinOrderDto extends StockInSemiFinOrder {

    /**
     * 半成品入库明细dto集合
     */
    private List<StockInSemiFinOrderDetailDto> stockInSemiFinOrderDetailDtoList;

    /**
     * 删除的明细id集合
     */
    private Long[] deleteIds;

    /**
     * 删除的明细id集合
     */
    private String[] detailStatusArr;

    /**
     * 生产订单号
     */
    private String prdOrderNo;

    public String getPrdOrderNo() {
        return prdOrderNo;
    }

    public void setPrdOrderNo(String prdOrderNo) {
        this.prdOrderNo = prdOrderNo;
    }

    public Long[] getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(Long[] deleteIds) {
        this.deleteIds = deleteIds;
    }

    public List<StockInSemiFinOrderDetailDto> getStockInSemiFinOrderDetailDtoList() {
        return stockInSemiFinOrderDetailDtoList;
    }

    public void setStockInSemiFinOrderDetailDtoList(List<StockInSemiFinOrderDetailDto> stockInSemiFinOrderDetailDtoList) {
        this.stockInSemiFinOrderDetailDtoList = stockInSemiFinOrderDetailDtoList;
    }

    public String[] getDetailStatusArr() {
        return detailStatusArr;
    }

    public void setDetailStatusArr(String[] detailStatusArr) {
        this.detailStatusArr = detailStatusArr;
    }
}
