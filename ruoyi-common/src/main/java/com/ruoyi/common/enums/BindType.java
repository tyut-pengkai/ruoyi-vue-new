package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BindType implements BaseEnum {

    NONE("0", "不绑定/无限制"),
    ONE_TO_ONE("1", "用户与设备一对一绑定"),
    ONE_TO_MANY("2", "一用户可绑定多个设备"),
    MANY_TO_ONE("3", "多用户可绑定同一设备");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

