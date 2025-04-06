package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-06 18:04
 */
@Getter
@AllArgsConstructor
public enum EOrderTargetTypeAction {

    ORDER(1, "订单"),
    ORDER_DETAIL(2, "订单明细"),
    ;

    private final Integer value;
    private final String label;

    public static EOrderTargetTypeAction of(Integer value) {
        for (EOrderTargetTypeAction e : EOrderTargetTypeAction.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
