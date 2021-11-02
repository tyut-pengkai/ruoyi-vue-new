package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LimitOper {
    TIPS("0", "提示用户"), EARLIEST_LOGOUT("1", "注销最早登录的用户");

    private final String code;
    private final String info;
}
