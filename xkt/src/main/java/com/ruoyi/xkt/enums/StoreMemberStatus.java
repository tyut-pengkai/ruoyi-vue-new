package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员等级状态
 *
 * @author liujiang
 * @date 2025-11-16 23:42
 */
@Getter
@AllArgsConstructor
public enum StoreMemberStatus {

    // 未购买待审核
    WAIT_AUDIT(1, "未购买待审核"),
    // 已购买待审核（续费状态）
    BOUGHT_WAIT_AUDIT(2, "已购买待审核"),
    // 审核通过（正式成为会员）
    AUDIT_PASS(3, "审核通过"),
    // 审核拒绝
    AUDIT_REJECT(4, "审核拒绝"),

    ;

    private final Integer value;
    private final String label;

    public static StoreMemberStatus of(Integer value) {
        for (StoreMemberStatus e : StoreMemberStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("会员状态不存在!", HttpStatus.ERROR);
    }

}
