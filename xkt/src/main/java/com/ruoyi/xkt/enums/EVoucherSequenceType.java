package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EVoucherSequenceType {

    STORE_SALE("STORE_SALE", ""),
    STORAGE("STORAGE", ""),
    DEMAND("DEMAND", ""),
    STORE_ORDER("STORE_ORDER", "代发订单");

    private final String value;
    private final String label;

    public static EVoucherSequenceType of(String value) {
        for (EVoucherSequenceType e : EVoucherSequenceType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
