package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-05-28 17:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuInfo extends SysMenu {
    /**
     * 角色ID，角色菜单关联表字段
     */
    private Long relRoleId;

}
