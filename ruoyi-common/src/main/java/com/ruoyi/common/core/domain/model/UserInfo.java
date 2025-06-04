package com.ruoyi.common.core.domain.model;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * @author liangyq
 * @date 2025-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserInfo extends SysUser {

    /**
     * 角色集
     */
    private List<RoleInfo> roles;

    /**
     * 菜单集
     */
    private Set<SysMenu> menus;

    public UserInfo updateBaseInfo(SysUser user) {
        BeanUtil.copyProperties(user, this);
        return this;
    }

}
