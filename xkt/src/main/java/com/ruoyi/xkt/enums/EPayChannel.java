package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EPayChannel {

    ALI_PAY(1, "支付宝");

    private final Integer value;
    private final String label;

    public static EPayChannel of(Integer value) {
        for (EPayChannel e : EPayChannel.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
