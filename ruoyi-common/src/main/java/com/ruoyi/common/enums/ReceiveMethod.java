package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiveMethod implements BaseEnum {
    ALIPAY("alipay", "支付宝"),
    WECHAT("wechat", "微信"),
    QQ("qq", "QQ钱包"),
    BANK_CARD("bank_card", "银行卡"),
    ;

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

