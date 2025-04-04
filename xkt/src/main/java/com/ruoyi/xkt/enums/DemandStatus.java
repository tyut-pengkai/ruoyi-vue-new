package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 刘江
 * @date 2025-04-04 23:42
 */
@Getter
@AllArgsConstructor
public enum DemandStatus {

    // 待生产
    PENDING_PRODUCTION(1, "待生产"),
    // 生产中
    IN_PRODUCTION(2, "生产中"),
    // 生产完成
    PRODUCTION_COMPLETE(3, "生产完成");

    private final Integer value;
    private final String label;

    public static DemandStatus of(Integer value) {
        for (DemandStatus e : DemandStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Enum not defined: " + value);
    }
}
