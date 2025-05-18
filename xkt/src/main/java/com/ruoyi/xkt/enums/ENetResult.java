package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-05-18 14:44
 */
@Getter
@AllArgsConstructor
public enum ENetResult {
    
    SUCCESS(1, "成功"),
    FAILURE(2, "失败"),
    WAIT(3, "等待");

    private final Integer value;
    private final String label;

    public static ENetResult of(Integer value) {
        for (ENetResult e : ENetResult.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
