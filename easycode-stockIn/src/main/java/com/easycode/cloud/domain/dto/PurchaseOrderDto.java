package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.PurchaseOrder;
import org.apache.ibatis.type.Alias;

/**
 * 采购单dto
 * @author bcp
 */
@Alias("PurchaseOrderDto")
public class PurchaseOrderDto extends PurchaseOrder {

    //采购单集合
    private String[] orderNos;

    public String[] getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(String[] orderNos) {
        this.orderNos = orderNos;
    }
}
