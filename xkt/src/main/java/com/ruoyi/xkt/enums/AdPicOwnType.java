package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广竞价商品图片归属类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdPicOwnType {

    // 档口
    STORE(0, "档口"),
    // 系统
    SYSTEM(1, "系统"),

    ;

    private final Integer value;
    private final String label;

    public static AdPicOwnType of(Integer value) {
        for (AdPicOwnType e : AdPicOwnType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广竞价状态不存在!", HttpStatus.ERROR);
    }
}
