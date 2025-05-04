package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销展示类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdDisplayType {

    // 推广图
    PICTURE(1, "推广图"),
    // 商品
    PRODUCT(2, "商品"),
    // 图和商品
    PIC_AND_PROD(3, "图和商品"),


    ;

    private final Integer value;
    private final String label;

    public static AdDisplayType of(Integer value) {
        for (AdDisplayType e : AdDisplayType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
