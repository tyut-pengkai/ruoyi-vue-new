package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType implements BaseEnum {

    SALE("1", "购卡订单"),
    CHARGE("2", "充值订单");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
