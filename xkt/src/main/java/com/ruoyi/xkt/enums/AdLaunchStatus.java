package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销每一轮播放状态
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdLaunchStatus {

    // 投放中
    LAUNCHING(1, "投放中"),
    // 待投放
    UN_LAUNCH(2, "待投放"),
    // 已过期
    EXPIRED(3, "已过期"),
    // 已退订
    CANCEL(4, "已退订"),


    ;

    private final Integer value;
    private final String label;

    public static AdLaunchStatus of(Integer value) {
        for (AdLaunchStatus e : AdLaunchStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广投放状态不存在!", HttpStatus.ERROR);
    }
}
