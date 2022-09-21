package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchOperationObject implements BaseEnum {

    CARD("0", "卡密"),
    LOGIN_CODE("1", "单码"),
    ACCOUNT_USER("2", "账号用户"),
    LOGIN_CODE_USER("3", "单码用户"),
    ;

    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

}
