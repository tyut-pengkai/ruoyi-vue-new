package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SaleOrderStatus implements BaseEnum {
    /**
     * 枚举名称	枚举说明	触发条件描述
     * WAIT_PAY	交易创建，等待买家付款	交易创建
     * TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款	交易关闭
     * TRADE_SUCCESS	交易支付成功，可退款	支付成功
     * TRADE_FINISHED	交易结束，不可退款	交易完成
     */
    WAIT_PAY("0", "待付款"),
    PAID("1", "已付款"),
    TRADE_CLOSED("2", "交易关闭"),
    TRADE_SUCCESS("3", "交易成功"),
    TRADE_FINISHED("4", "交易结束");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
