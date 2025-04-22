package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-22 13:42
 */
@Getter
@AllArgsConstructor
public enum EAccountOwnerType {

    PLATFORM(1, "平台"),
    STORE(2, "档口"),
    USER(3, "用户"),
    ;

    private final Integer value;
    private final String label;

    public static EAccountOwnerType of(Integer value) {
        for (EAccountOwnerType e : EAccountOwnerType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
