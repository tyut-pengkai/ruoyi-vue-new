package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EProductStatus {

    UN_PUBLISHED("UN_PUBLISHED", "未发布"),
    ON_SALE("ON_SALE", "在售"),
    TAIL_GOODS("TAIL_GOODS", "尾货"),
    OFF_SALE("OFF_SALE", "已下架"),
    REMOVED("REMOVED", "已删除");

    private final String value;
    private final String label;

    public static EProductStatus of(String value) {
        for (EProductStatus e : EProductStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 是否允许下单
     *
     * @param value
     * @return
     */
    public static boolean accessOrder(String value) {
        return ON_SALE.getValue().equals(value) || TAIL_GOODS.getValue().equals(value);
    }
}
