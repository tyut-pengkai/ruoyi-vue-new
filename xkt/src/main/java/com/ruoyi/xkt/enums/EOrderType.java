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

    private Integer value;
    private String label;

    public static EOrderType of(String value) {
        for (EOrderType e : EOrderType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
