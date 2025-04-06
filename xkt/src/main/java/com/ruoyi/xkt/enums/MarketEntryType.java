package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 营业执照 市场主体类型
 * @author 刘江
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum MarketEntryType {

    ENTERPRISE (1, "企业"),
    FARMER_COOPERATIVE (2, "农民专业合作社"),
    SOLE_PROPRIETORSHIP (3, "个体工商户"),
    NATURAL_PERSON (4, "自然人"),
    GOVERNMENT_INSTITUTION (5, "机关事业单位"),
    OTHER (0, "其它"),

    ;

    private final Integer value;
    private final String label;

    public static MarketEntryType of(Integer value) {
        for (MarketEntryType e : MarketEntryType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
