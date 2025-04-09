package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EAccountStatus {

    NORMAL(1, "正常"),
    FREEZE(2, "冻结");

    private final Integer value;
    private final String label;

    public static EAccountStatus of(Integer value) {
        for (EAccountStatus e : EAccountStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
