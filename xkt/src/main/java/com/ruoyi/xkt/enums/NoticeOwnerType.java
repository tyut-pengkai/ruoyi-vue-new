package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 谁发布公告类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum NoticeOwnerType {

    // 档口
    STORE(1, "档口"),
    // 系统
    SYSTEM(2, "系统"),


    ;

    private final Integer value;
    private final String label;

    public static NoticeOwnerType of(Integer value) {
        for (NoticeOwnerType e : NoticeOwnerType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("谁发的公告类型不存在!", HttpStatus.ERROR);
    }
}
