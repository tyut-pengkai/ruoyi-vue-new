package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EExpressChannel {

    ZTO(1, "中通", 1L, "ZTO");

    private final Integer value;
    private final String label;
    private final Long expressId;
    private final String expressCode;

    public static EExpressChannel of(Integer value) {
        for (EExpressChannel e : EExpressChannel.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
