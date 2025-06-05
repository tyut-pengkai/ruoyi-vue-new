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
public enum NoticeReadType {

    // 未读
    UN_READ(0, "未读"),
    // 已读
    READ(1, "已读"),


    ;

    private final Integer value;
    private final String label;

    public static NoticeReadType of(Integer value) {
        for (NoticeReadType e : NoticeReadType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广展示类型不存在!", HttpStatus.ERROR);
    }
}
