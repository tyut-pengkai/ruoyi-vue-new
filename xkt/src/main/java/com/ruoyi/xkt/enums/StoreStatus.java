package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum StoreStatus {

    REGISTERED(1, "已注册"),
    AUTH_LICENSE(2, "认证营业执照"),
    AUTH_BASE_INFO(3, "认证基本信息"),
    UN_AUDITED(4, "待审核"),
    AUDIT_REJECTED(5, "审核驳回"),
    TRIAL_PERIOD(6, "试用期"),
    FORMAL_USE(7, "正式使用"),
    CLEARANCE(8, "强制清退")

    ;

    private final Integer value;
    private final String label;

    public static StoreStatus of(Integer value) {
        for (StoreStatus e : StoreStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口状态类型不存在!", HttpStatus.ERROR);
    }
}
