package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销上线类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdOnlineStatus {

    // 已上线
    ONLINE(1, "已上线"),
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
        throw new ServiceException("营销推广上线状态不存在!", HttpStatus.ERROR);
    }
}
