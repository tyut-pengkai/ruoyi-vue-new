package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {

    ACCOUNT("0", "账号登录"), CODE("1", "登录码登录");

    private final String code;
    private final String info;

}
