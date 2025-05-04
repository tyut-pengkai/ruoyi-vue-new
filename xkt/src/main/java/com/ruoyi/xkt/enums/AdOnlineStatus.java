package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdOnlineStatus {

    // 上线中
    ONLINE(1, "上线中"),
    // 已下线
    OFFLINE(2, "已下线"),


    ;

    private final Integer value;
    private final String label;

    public static AdOnlineStatus of(Integer value) {
        for (AdOnlineStatus e : AdOnlineStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
