package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {

    SETTLED(1, "已结清"),
    DEBT(2, "欠款");

    private final Integer value;
    private final String label;

    public static PaymentStatus of(Integer value) {
        for (PaymentStatus e : PaymentStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}
