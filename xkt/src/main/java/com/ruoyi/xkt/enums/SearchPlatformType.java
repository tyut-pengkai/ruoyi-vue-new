package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户搜索内容的平台
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum SearchPlatformType {

    // 电脑端
    PC(1, "PC"),
    // APP
    APP(2, "APP"),


    ;

    private final Integer value;
    private final String label;

    public static SearchPlatformType of(Integer value) {
        for (SearchPlatformType e : SearchPlatformType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广展示类型不存在!", HttpStatus.ERROR);
    }
}
