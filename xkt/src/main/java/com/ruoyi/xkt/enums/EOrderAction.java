package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-06 18:04
 */
@Getter
@AllArgsConstructor
public enum EOrderAction {

    ADD_ORDER(1, "下单"),
    ;

    private final Integer value;
    private final String label;

    public static EOrderAction of(Integer value) {
        for (EOrderAction e : EOrderAction.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
