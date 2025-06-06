package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知公告接收类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum NoticeReceiveType {

    // 不接收
    UN_RECEIVE(0, "不接收"),
    // 接收
    RECEIVE(1, "接收"),


    ;

    private final Integer value;
    private final String label;

    public static NoticeReceiveType of(Integer value) {
        for (NoticeReceiveType e : NoticeReceiveType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("通知公告接收类型不存在!", HttpStatus.ERROR);
    }
}
