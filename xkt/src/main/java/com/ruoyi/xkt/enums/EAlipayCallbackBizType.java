package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-05-13 14:39
 */
@Getter
@AllArgsConstructor
public enum EAlipayCallbackBizType {

    ORDER_PAY(1, "订单支付"),
    RECHARGE(2, "充值"),
    UNKNOWN(3, "未知"),
    ;

    private final Integer value;
    private final String label;

    public static EAlipayCallbackBizType of(Integer value) {
        for (EAlipayCallbackBizType e : EAlipayCallbackBizType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
