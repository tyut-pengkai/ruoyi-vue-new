package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeType implements BaseEnum {

    LOGIN("0", "直接登录"),
    CHARGE("1", "以卡充卡");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
