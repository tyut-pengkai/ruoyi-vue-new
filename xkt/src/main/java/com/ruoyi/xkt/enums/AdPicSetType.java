package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销图片设置类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdPicSetType {

    // 未设置
    UN_SET(1, "未设置"),
    // 已设置
    SET(2, "已设置"),


    ;

    private final Integer value;
    private final String label;

    public static AdPicSetType of(Integer value) {
        for (AdPicSetType e : AdPicSetType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广图片是否设置不存在!", HttpStatus.ERROR);
    }
}
