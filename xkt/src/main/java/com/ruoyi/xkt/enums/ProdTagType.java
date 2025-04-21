package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 档口标签类型
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum ProdTagType {

    // 当月爆款
    MONTH_HOT(1, "当月爆款"),
    // 档口爆款
    STORE_HOT(2, "档口热卖"),
    // 三日上新
    THREE_DAY_NEW(3, "三日上新"),
    // 七日上新
    SEVEN_DAY_NEW(4, "七日上新"),
    // 风格
    STYLE(5, "风格"),


    ;


    private final Integer value;
    private final String label;

    public static ProdTagType of(Integer value) {
        for (ProdTagType e : ProdTagType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
