package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EFinBillType {

    COLLECTION(1, "收款单"),
    PAYMENT(2, "付款单"),
    TRANSFER(3, "转移单");

    private final Integer value;
    private final String label;

    public static EFinBillType of(Integer value) {
        for (EFinBillType e : EFinBillType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
