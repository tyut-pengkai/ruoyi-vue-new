package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillType implements BaseEnum {

    TIME("0", "计时模式"),
    POINT("1", "计点模式");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
