package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销竞价状态
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdRoundType {

    // 播放轮
    PLAY_ROUND(1, "播放轮"),
    // 第二轮
    SECOND_ROUND(2, "第二轮"),
    // 第三轮
    THIRD_ROUND(3, "第三轮"),
    // 第四轮
    FOURTH_ROUND(4, "第四轮"),


    ;

    private final Integer value;
    private final String label;

    public static AdRoundType of(Integer value) {
        for (AdRoundType e : AdRoundType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广播放轮次不存在!", HttpStatus.ERROR);
    }
}
