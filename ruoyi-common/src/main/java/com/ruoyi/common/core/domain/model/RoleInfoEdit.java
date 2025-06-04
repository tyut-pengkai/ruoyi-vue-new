package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-29 16:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleInfoEdit extends SysRole {
    /**
     * 菜单ID集
     */
    private List<Long> menuIds;
}
