package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EEntryExecuted {

    FINISH(1, "已执行"),
    WAITING(2, "未执行");

    private final Integer value;
    private final String label;

    public static EEntryExecuted of(Integer value) {
        for (EEntryExecuted e : EEntryExecuted.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
