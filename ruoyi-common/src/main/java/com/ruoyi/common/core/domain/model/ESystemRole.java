package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-05-27
 */
@Getter
@AllArgsConstructor
public enum ESystemRole {


    SUPER_ADMIN(1, "超级管理员", 1L),
    GENERAL_ADMIN(2, "普通管理员", 2L),
    SUPPLIER(3, "档口供应商", 3L),
    SELLER(4, "电商卖家", 4L);

    private final Integer value;
    private final String label;
    private final Long id;

    public static ESystemRole of(Integer value) {
        for (ESystemRole e : ESystemRole.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static ESystemRole of(Long id) {
        for (ESystemRole e : ESystemRole.values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isDefaultRole(Long roleId) {
        for (ESystemRole e : ESystemRole.values()) {
            if (e.getId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }
}
