package com.ruoyi.xkt.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-03 13:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderAddResult {
    /**
     * 订单信息
     */
    private StoreOrderExt orderExt;
    /**
     * 三方支付返回信息
     */
    private String payRtnStr;
}
