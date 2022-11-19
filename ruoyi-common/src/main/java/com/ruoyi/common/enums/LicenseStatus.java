package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LicenseStatus implements BaseEnum {

    NORMAL("0", "正常"),
    MOVE("1", "转移"),
    REMOVE("2", "移除");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
