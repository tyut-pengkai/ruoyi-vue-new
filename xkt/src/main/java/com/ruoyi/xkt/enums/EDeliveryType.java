package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EDeliveryType {

    SHIP_COMPLETE(1, "货齐再发"),
    PARTIAL_SHIPMENT(2, "有货先发");

    private Integer value;
    private String label;

    public static EDeliveryType of(String value) {
        for (EDeliveryType e : EDeliveryType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
