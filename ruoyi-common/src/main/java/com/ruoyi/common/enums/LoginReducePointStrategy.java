package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginReducePointStrategy implements BaseEnum {

    PARAMS("0", "以接口传参为准"),
    ALWAYS("1", "总是扣除1点"),
    NONE("2", "不进行扣除");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
