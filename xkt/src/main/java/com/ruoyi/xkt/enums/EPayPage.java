package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-07 10:01
 */
@Getter
@AllArgsConstructor
public enum EPayPage {

    WEB(1, "电脑网站"),
    WAP(2, "手机网站"),
    ;

    private final Integer value;
    private final String label;

    public static EPayPage of(Integer value) {
        for (EPayPage e : EPayPage.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
