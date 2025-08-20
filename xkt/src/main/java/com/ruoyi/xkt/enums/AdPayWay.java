package com.ruoyi.xkt.enums;

import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdPayWay {

    BALANCE(1, "余额支付"),
    ALIPAY(2, "支付宝支付"),

    ;

    private final Integer value;
    private final String label;

    public static AdPayWay of(Integer value) {
        for (AdPayWay e : AdPayWay.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("推广支付方式不存在!");
    }
}
