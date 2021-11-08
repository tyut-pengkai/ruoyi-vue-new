package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenRule implements BaseEnum {

    NUM_UPPERCASE_LOWERCASE("0", "数字+大写字母+小写字母"),
    NUM_UPPERCASE("1", "数字+大写字母"),
    NUM_LOWERCASE("2", "数字+小写字母"),
    UPPERCASE_LOWERCASE("3", "大写字母+小写字母"),
    UPPERCASE("4", "大写字母"),
    LOWERCASE("5", "小写字母"),
    NUM("6", "数字"),
    REGEX("7", "自定义规则（正则）");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
