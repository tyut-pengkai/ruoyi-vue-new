package com.easycode.cloud.domain.vo;

import com.weifu.cloud.domain.StockInStdOrderDetail;
import org.apache.ibatis.type.Alias;

/**
 *  标准入库单明细vo
 * @author bcp
 */
@Alias("StockInStdOrderDetailVo")
public class StockInStdOrderDetailVo extends StockInStdOrderDetail {

    /**
     * 标准入库单号
     */
    private String orderNo;

    /**
     * 工厂id
     */
    private String factoryId;

    /**
     * 工厂code
     */
    private String factoryCode ;

    /**
     * 替代送货数量
     */
    private Long tempDeliverQty;



    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public Long getTempDeliverQty() {
        return tempDeliverQty;
    }

    public void setTempDeliverQty(Long tempDeliverQty) {
        this.tempDeliverQty = tempDeliverQty;
    }
}
