package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemplateType implements BaseEnum {

    CHARGE_CARD("1", "充值卡"),
    LOGIN_CODE("2", "登录码");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
