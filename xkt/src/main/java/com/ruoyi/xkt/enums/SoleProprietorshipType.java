package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 营业执照 经营类型
 * @author 刘江
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum SoleProprietorshipType {

    SOLO_PROPRIETORSHIP(1, "个人独资企业"),
    SOLO_PROPRIETORSHIP_BRANCH(2, "个人独资企业分支机构"),

    ;

    private final Integer value;
    private final String label;

    public static SoleProprietorshipType of(Integer value) {
        for (SoleProprietorshipType e : SoleProprietorshipType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口营业执照 经营类型不存在!", HttpStatus.ERROR);
    }
}
