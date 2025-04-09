package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EFinBillRelType {

    STORE_ORDER(1, "代发订单");

    private final Integer value;
    private final String label;

    public static EFinBillRelType of(Integer value) {
        for (EFinBillRelType e : EFinBillRelType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
