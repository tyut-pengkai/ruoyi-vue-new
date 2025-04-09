package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum ELoanDirection {

    DEBIT(1, "借", "D"),
    CREDIT(2, "贷", "C");

    private final Integer value;
    private final String label;
    private final String code;

    public static ELoanDirection of(Integer value) {
        for (ELoanDirection e : ELoanDirection.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
