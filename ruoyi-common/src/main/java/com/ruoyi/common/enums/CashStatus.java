package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashStatus implements BaseEnum {
    WAIT_AUDIT("1", "待审核"),
    AUDIT_PASS("2", "审核通过"),
    AUDIT_REFUSE("3", "审核不通过"),
    WAIT_TRANSFER("4", "待打款"),
    TRANSFERRED("5", "已打款"),
    TRANSFER_FAILED("6", "打款失败");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

