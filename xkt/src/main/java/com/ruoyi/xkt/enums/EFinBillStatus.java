package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EFinBillStatus {

    INIT(1, "初始"),
    PROCESSING(2, "执行中"),
    SUCCESS(3, "执行成功"),
    FAILURE(4, "执行失败");

    private final Integer value;
    private final String label;

    public static EFinBillStatus of(Integer value) {
        for (EFinBillStatus e : EFinBillStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
