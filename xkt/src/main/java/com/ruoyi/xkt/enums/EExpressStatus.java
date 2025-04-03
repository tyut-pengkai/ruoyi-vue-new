package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EExpressStatus {

    INIT(1, "初始"),
    PLACING(2, "下单中"),
    PLACED(3, "已下单"),
    CANCELLING(4, "取消中"),
    PICKED_UP(5, "已揽件"),
    INTERCEPTING(6, "拦截中"),
    COMPLETED(99, "已结束");

    private final Integer value;
    private final String label;

    public static EExpressStatus of(Integer value) {
        for (EExpressStatus e : EExpressStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
