package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销平台类型
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdPlatformType {

    PC(1, "电脑端"),
    APP(2, "APP"),

    ;

    private final Integer value;
    private final String label;

    public static AdPlatformType of(Integer value) {
        for (AdPlatformType e : AdPlatformType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
