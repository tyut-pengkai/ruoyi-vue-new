package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum ListingType {

    RIGHT_NOW(1, "立即上架"),
    TIME_SCHEDULE(2, "定时上架");

    private final Integer value;
    private final String label;

    public static ListingType of(Integer value) {
        for (ListingType e : ListingType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
