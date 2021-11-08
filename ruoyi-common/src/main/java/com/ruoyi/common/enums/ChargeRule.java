package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeRule implements BaseEnum {

    NONE("0", "无限制"),
    EXPIRE_REQUIRED("1", "只能用于到期账号");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
