package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdDiscountType {

    // 现金
    CASH(1, "现金"),
    // 折扣
    DISCOUNT(2, "折扣"),

    ;

    private final Integer value;
    private final String label;

    public static AdDiscountType of(Integer value) {
        for (AdDiscountType e : AdDiscountType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("推广营销折扣类型不存在!", HttpStatus.ERROR);
    }
}
