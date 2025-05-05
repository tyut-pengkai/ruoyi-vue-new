package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销图片设计类型
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdDesignType {

    // 自主设计
    STORE_DESIGN(1, "自主设计"),
    // 平台设计
    SYS_DESIGN(2, "平台设计");



    ;

    private final Integer value;
    private final String label;

    public static AdDesignType of(Integer value) {
        for (AdDesignType e : AdDesignType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("推广营销图片设计类型不存在!", HttpStatus.ERROR);
    }
}
