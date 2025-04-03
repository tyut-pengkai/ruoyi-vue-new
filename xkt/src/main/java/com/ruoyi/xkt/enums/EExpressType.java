package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EExpressType {

    PLATFORM(1, "平台物流"),
    STORE(2, "档口物流");

    private final Integer value;
    private final String label;

    public static EExpressType of(Integer value) {
        for (EExpressType e : EExpressType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
