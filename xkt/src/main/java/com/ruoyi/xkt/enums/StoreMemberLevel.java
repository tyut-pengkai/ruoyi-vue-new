package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 档口会员类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum StoreMemberLevel {

    // 实力质造
    STRENGTH_CONSTRUCT(1, "实力质造"),


    ;

    private final Integer value;
    private final String label;

    public static StoreMemberLevel of(Integer value) {
        for (StoreMemberLevel e : StoreMemberLevel.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口会员类型不存在!", HttpStatus.ERROR);
    }
}
