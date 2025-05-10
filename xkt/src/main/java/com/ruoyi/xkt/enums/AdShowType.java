package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销展示类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdShowType {

    // 时间范围
    TIME_RANGE(1, "时间范围"),
    // 位置枚举
    POSITION_ENUM(2, "位置枚举"),


    ;

    private final Integer value;
    private final String label;

    public static AdShowType of(Integer value) {
        for (AdShowType e : AdShowType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广播放轮次不存在!", HttpStatus.ERROR);
    }
}
