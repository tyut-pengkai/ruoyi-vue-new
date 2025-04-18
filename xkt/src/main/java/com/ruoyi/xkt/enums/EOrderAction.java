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
