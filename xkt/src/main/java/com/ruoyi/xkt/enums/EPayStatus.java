package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EPayStatus {

    INIT(1, "初始化"),
    PAYING(2, "支付中"),
    PAID(3, "已支付");

    private Integer value;
    private String label;

    public static EPayStatus of(String value) {
        for (EPayStatus e : EPayStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
