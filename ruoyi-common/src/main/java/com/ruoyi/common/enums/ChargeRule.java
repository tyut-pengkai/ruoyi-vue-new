package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeRule {

    NONE("0", "无限制"), EXPIRE_REQUIRED("1", "只能用于到期账号");

    private final String code;
    private final String info;
}
