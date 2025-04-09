package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EEntryStatus {

    FINISH(1, "已入账"),
    WAITING(2, "待入账");

    private final Integer value;
    private final String label;

    public static EEntryStatus of(Integer value) {
        for (EEntryStatus e : EEntryStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
