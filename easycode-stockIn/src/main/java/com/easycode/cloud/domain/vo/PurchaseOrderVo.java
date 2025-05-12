package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.PurchaseOrder;
import org.apache.ibatis.type.Alias;

@Alias("PurchaseOrderVo")
public class PurchaseOrderVo extends PurchaseOrder {

    private Long orderNum;

    private Long maxId;

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }
}
