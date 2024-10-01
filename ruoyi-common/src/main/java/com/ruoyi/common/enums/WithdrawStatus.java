package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WithdrawStatus implements BaseEnum {
    /**
     * 0：待审核，1：审核通过，待打款，2：审核不通过，3：提现成功，已打款， 4：打款失败
     */
    WAIT_PAY("0", "待打款"),
    PAY_SUCCESS("1", "提现成功"),
    PAY_FAILED("2", "提现失败"),
    USER_CANCELED("3", "用户撤销");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
