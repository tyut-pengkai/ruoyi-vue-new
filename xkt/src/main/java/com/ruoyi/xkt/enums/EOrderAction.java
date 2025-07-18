package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-06 18:04
 */
@Getter
@AllArgsConstructor
public enum EOrderAction {

    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    PAY(4, "支付"),
    CANCEL(5, "取消"),
    SHIP(6, "发货"),
    COMPLETE(7, "完成"),
    APPLY_AFTER_SALE(8, "申请售后"),
    REJECT_AFTER_SALE(9, "拒绝售后"),
    CONFIRM_AFTER_SALE(10, "确认售后"),
    REFUND(11, "退款"),
    APPLY_PLATFORM_INVOLVE(12, "申请平台介入"),
    COMPLETE_PLATFORM_INVOLVE(13, "平台介入完成"),
    USER_COMPLETE_AFTER_SALE(14, "用户确认售后完成"),
    EXPRESS_SHIP(15, "物流发货"),
    ;

    private final Integer value;
    private final String label;

    public static EOrderAction of(Integer value) {
        for (EOrderAction e : EOrderAction.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
