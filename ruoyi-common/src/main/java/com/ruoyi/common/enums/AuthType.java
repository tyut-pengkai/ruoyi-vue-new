package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType implements BaseEnum {

    ACCOUNT("0", "账号登录"),
    CODE("1", "登录码登录");
    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

}
