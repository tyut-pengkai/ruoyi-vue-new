package com.ruoyi.xkt.enums;

import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-22 13:42
 */
@Getter
@AllArgsConstructor
public enum EAccountType {

    ALI_PAY(1, "支付宝"),
    ;

    private final Integer value;
    private final String label;

    public static EAccountType of(Integer value) {
        for (EAccountType e : EAccountType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取与支付通道匹配的账户类型
     *
     * @param payChannel
     * @return
     */
    public static EAccountType getByChannel(EPayChannel payChannel) {
        if (EPayChannel.ALI_PAY == payChannel) {
            return ALI_PAY;
        }
        throw new ServiceException("无法获取与支付通道匹配的账户类型");
    }
}
