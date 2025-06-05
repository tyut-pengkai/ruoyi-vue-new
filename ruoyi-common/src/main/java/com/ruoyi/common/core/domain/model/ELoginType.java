package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-05-27
 */
@Getter
@AllArgsConstructor
public enum ELoginType {


    USERNAME(1, "用户名登录"),
    SMS_VERIFICATION_CODE(2, "短信验证码登录");

    private final Integer value;
    private final String label;

    public static ELoginType of(Integer value) {
        for (ELoginType e : ELoginType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
