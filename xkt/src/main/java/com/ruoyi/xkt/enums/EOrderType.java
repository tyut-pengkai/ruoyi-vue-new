package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EOrderType {

    SALES_ORDER(1, "销售订单"),
    RETURN_ORDER(2, "退货订单");

    private final Integer value;
    private final String label;

    public static EOrderType of(Integer value) {
        for (EOrderType e : EOrderType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
