package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum ProductSizeStatus {

    STANDARD(1, "标准码"),
    UN_STANDARD(2, "非标准码");

    private final Integer value;
    private final String label;

    public static ProductSizeStatus of(Integer value) {
        for (ProductSizeStatus e : ProductSizeStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}
