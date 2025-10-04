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
public enum EProductItemType {

    // 非私款
    NON_PRIVATE_ITEM(0, "非私款"),
    // 私款
    PRIVATE_ITEM(1, "私款"),

    ;

    private final Integer value;
    private final String label;

    public static EProductItemType of(Integer value) {
        for (EProductItemType e : EProductItemType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("未知商品私款状态", HttpStatus.ERROR);
    }

}
