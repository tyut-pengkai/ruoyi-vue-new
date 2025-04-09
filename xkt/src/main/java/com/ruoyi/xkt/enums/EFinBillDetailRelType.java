package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EFinBillDetailRelType {

    STORE_ORDER_DETAIL(1, "代发订单明细");

    private final Integer value;
    private final String label;

    public static EFinBillDetailRelType of(Integer value) {
        for (EFinBillDetailRelType e : EFinBillDetailRelType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
