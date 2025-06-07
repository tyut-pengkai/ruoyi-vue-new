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
public enum EProductStatus {

    UN_PUBLISHED(1, "未发布"),
    ON_SALE(2, "在售"),
    TAIL_GOODS(3, "尾货"),
    OFF_SALE(4, "已下架"),
    REMOVED(5, "已删除");

    private final Integer value;
    private final String label;

    public static EProductStatus of(Integer value) {
        for (EProductStatus e : EProductStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("未知商品状态", HttpStatus.ERROR);
    }

    /**
     * 是否允许下单
     *
     * @param value
     * @return
     */
    public static boolean accessOrder(Integer value) {
        return ON_SALE.getValue().equals(value) || TAIL_GOODS.getValue().equals(value);
    }
}
