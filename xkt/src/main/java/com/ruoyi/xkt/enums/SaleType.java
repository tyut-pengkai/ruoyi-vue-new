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
public enum SaleType {

    GENERAL_SALE(1, "销售"),
    SALE_REFUND(2, "退货"),
    SALE_AND_REFUND(3, "销售/退货");

    private final Integer value;
    private final String label;

    public static SaleType of(Integer value) {
        for (SaleType e : SaleType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口销售出库类型不存在!", HttpStatus.ERROR);
    }
}
