package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销系统拦截类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdSysInterceptType {

    // 未拦截
    UN_INTERCEPT(0, "未拦截"),
    // 已拦截
    INTERCEPT(1, "已拦截"),


    ;

    private final Integer value;
    private final String label;

    public static AdSysInterceptType of(Integer value) {
        for (AdSysInterceptType e : AdSysInterceptType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("推广营销系统拦截类型不存在!", HttpStatus.ERROR);
    }
}
