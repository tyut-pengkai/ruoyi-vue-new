package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-07 23:42
 */
@Getter
@AllArgsConstructor
public enum EProcessStatus {

    INIT(1, "初始"),
    PROCESSING(2, "处理中"),
    SUCCESS(3, "处理成功"),
    FAILURE(4, "处理失败"),
    NO_PROCESSING(5, "不处理");

    private final Integer value;
    private final String label;

    public static EProcessStatus of(Integer value) {
        for (EProcessStatus e : EProcessStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isSkip(Integer value) {
        return SUCCESS.getValue().equals(value)
                || FAILURE.getValue().equals(value)
                || NO_PROCESSING.getValue().equals(value);
    }
}
