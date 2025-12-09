package com.ruoyi.xkt.enums;

import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum PrivateItemType {

    // 是否私款 0 否 1 是
    NON_PRIVATE(0, "正常"),
    PRIVATE(1, "私款"),

    ;

    private final Integer value;
    private final String label;

    public static PrivateItemType of(Integer value) {
        for (PrivateItemType e : PrivateItemType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("是否私款不存在!");
    }
}
