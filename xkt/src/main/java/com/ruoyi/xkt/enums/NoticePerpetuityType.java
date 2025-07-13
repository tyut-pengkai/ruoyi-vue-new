package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum NoticePerpetuityType {

    // 存在有效期
    TEMPORARY(1, "存在有效期"),
    // 永久有效
    PERMANENT(2, "永久有效"),

    ;

    private final Integer value;
    private final String label;

    public static NoticePerpetuityType of(Integer value) {
        for (NoticePerpetuityType e : NoticePerpetuityType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("通知类型不存在!", HttpStatus.ERROR);
    }
}
