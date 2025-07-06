package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EmergencyType {

    PROD_STORAGE(1, "紧急单"),
    OTHER_STORAGE(0, "正常单");

    private final Integer value;
    private final String label;

    public static EmergencyType of(Integer value) {
        for (EmergencyType e : EmergencyType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
