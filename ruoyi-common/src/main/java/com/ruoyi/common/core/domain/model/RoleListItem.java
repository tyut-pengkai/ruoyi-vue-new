package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-05-29 14:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleListItem extends SysRole {
    /**
     * 档口名称
     */
    private String storeName;
}
