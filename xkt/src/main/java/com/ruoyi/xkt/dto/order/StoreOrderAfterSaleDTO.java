package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 16:17
 */
@Data
public class StoreOrderAfterSaleDTO {

    private Long storeOrderId;

    private List<Long> storeOrderDetailIds;

    private Long operatorId;

    private String refundReasonCode;
}
