package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceChangeType implements BaseEnum {
    WITHDRAW_CASH_FREEZE("1", "提现冻结"),
    WITHDRAW_CASH_SUCCESS("2", "提现成功"),
    WITHDRAW_CASH_CANCELED("3", "撤销提现解冻"),
    AGENT("4", "代理分成"),
    ADVISE("5", "推广分成"),
    TRANSFER_IN("6", "转账收入"),
    OTHOR_IN("7", "其他收入"),
    CONSUME("8", "消费支出"),
    TRANSFER_OUT("9", "转账支出"),
    OTHOR_OUT("10", "其他支出"),
    RECHARGE("11", "余额充值"),
    WITHDRAW_CASH_FAILED("12", "提现失败解冻");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

