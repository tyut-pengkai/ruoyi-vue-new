package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EFinBillSrcType {

    STORE_ORDER_PAID(1, "代发订单支付"),
    STORE_ORDER_COMPLETED(2, "代发订单完成"),
    WITHDRAW(3, "提现"),
    STORE_ORDER_REFUND(4, "代发订单退款"),
    ;

    private final Integer value;
    private final String label;

    public static EFinBillSrcType of(Integer value) {
        for (EFinBillSrcType e : EFinBillSrcType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
