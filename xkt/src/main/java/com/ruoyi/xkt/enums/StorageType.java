package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum StorageType {

    PROD_STORAGE(1, "生产入库"),
    OTHER_STORAGE(2, "其它入库"),
    REPAIR_STORAGE(3, "维修入库");

    private final Integer value;
    private final String label;

    public static StorageType of(Integer value) {
        for (StorageType e : StorageType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
