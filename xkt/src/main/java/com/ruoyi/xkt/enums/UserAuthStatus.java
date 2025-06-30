package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum UserAuthStatus {

    UN_AUDITED(1, "待审核"),
    AUDIT_REJECTED(2, "审核驳回"),
    FORMAL_USE(3, "正式使用"),

    ;

    private final Integer value;
    private final String label;

    public static UserAuthStatus of(Integer value) {
        for (UserAuthStatus e : UserAuthStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("代发审核类型不存在!", HttpStatus.ERROR);
    }
}
