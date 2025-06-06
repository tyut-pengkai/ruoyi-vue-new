package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知公告类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum NoticeType {

    // 通知
    NOTICE(1, "通知"),
    // 公告
    ANNOUNCEMENT(2, "公告"),


    ;

    private final Integer value;
    private final String label;

    public static NoticeType of(Integer value) {
        for (NoticeType e : NoticeType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("通知公告类型不存在!", HttpStatus.ERROR);
    }
}
