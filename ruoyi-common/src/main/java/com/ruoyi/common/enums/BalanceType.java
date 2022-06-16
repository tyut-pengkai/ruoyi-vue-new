package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceType implements BaseEnum {

    AVAILABLE_PAY_BALANCE("1", "可用充值余额"),
    FREEZE_PAY_BALANCE("2", "冻结充值余额"),
    AVAILABLE_FREE_BALANCE("3", "可用赠送余额"),
    FREEZE_FREE_BALANCE("4", "冻结赠送余额");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

