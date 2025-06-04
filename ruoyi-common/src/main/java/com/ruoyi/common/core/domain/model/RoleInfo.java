package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 17:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleInfo extends SysRole {
    /**
     * 角色ID，用户角色关联表字段
     */
    private Long relUserId;
    /**
     * 档口ID，用户角色关联表字段
     */
    private Long relStoreId;
    /**
     * 菜单集
     */
    private List<MenuInfo> menus;

}
