package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum PayWay {

    ALIPAY(1, "支付宝"),
    WECHAT_PAY(2, "微信支付"),
    CASH(3, "现金"),
    DEBT(4, "欠款");

    private final Integer value;
    private final String label;

    public static PayWay of(Integer value) {
        for (PayWay e : PayWay.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
