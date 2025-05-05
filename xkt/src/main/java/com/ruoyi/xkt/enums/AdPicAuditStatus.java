package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销图片审核状态
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdPicAuditStatus {

    // 待审核
    UN_AUDIT(1, "待审核"),
    // 审核通过
    AUDIT_PASS(2, "审核通过"),
    // 审核驳回
    AUDIT_REJECTED(3, "审核驳回"),



    ;

    private final Integer value;
    private final String label;

    public static AdPicAuditStatus of(Integer value) {
        for (AdPicAuditStatus e : AdPicAuditStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广图片审核类型不存在!", HttpStatus.ERROR);
    }
}
