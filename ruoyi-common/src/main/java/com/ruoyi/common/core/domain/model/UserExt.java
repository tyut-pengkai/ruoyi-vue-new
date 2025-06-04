package com.ruoyi.common.core.domain.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liangyq
 * @date 2025-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserExt extends SysUser {
    /**
     * 角色ID集合
     */
    private Set<Long> roleIds;
    /**
     * 菜单ID集合
     */
    private Set<Long> menuIds;
    /**
     * 角色KEY集合
     */
    private Set<String> roleKeys;
    /**
     * 菜单权限集合
     */
    private Set<String> menuPerms;
    /**
     * 管理权限的档口ID集合
     */
    private Set<Long> managedStoreIds;
    /**
     * 子账号权限的档口ID集合
     */
    private Set<Long> subStoreIds;

    /**
     * 是否有超级管理员角色
     *
     * @return
     */
    public boolean hasSuperAdminRole() {
        return CollUtil.contains(roleIds, ESystemRole.SUPER_ADMIN.getId());
    }

    /**
     * 是否有普通管理员角色
     *
     * @return
     */
    public boolean hasGeneralAdminRole() {
        return CollUtil.contains(roleIds, ESystemRole.GENERAL_ADMIN.getId());
    }

    /**
     * 是否有供应商角色
     *
     * @return
     */
    public boolean hasSupplierRole() {
        return CollUtil.contains(roleIds, ESystemRole.SUPPLIER.getId());
    }

    /**
     * 是否有卖家角色
     *
     * @return
     */
    public boolean hasSellerRole() {
        return CollUtil.contains(roleIds, ESystemRole.SELLER.getId());
    }

    /**
     * 是否有供应商子角色
     *
     * @return
     */
    public boolean hasSupplierSubRole() {
        return CollUtil.isNotEmpty(subStoreIds);
    }


    public static UserExt create(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserExt userExt = BeanUtil.toBean(userInfo, UserExt.class);
        userExt.roleIds = new HashSet<>();
        userExt.menuIds = new HashSet<>();
        userExt.roleKeys = new HashSet<>();
        userExt.menuPerms = new HashSet<>();
        userExt.managedStoreIds = new HashSet<>();
        userExt.subStoreIds = new HashSet<>();
        for (RoleInfo role : CollUtil.emptyIfNull(userInfo.getRoles())) {
            userExt.roleIds.add(role.getRoleId());
            if (StrUtil.isNotEmpty(role.getRoleKey())) {
                userExt.roleKeys.add(role.getRoleKey());
            }
            if (ESystemRole.SELLER.getId().equals(role.getRoleId()) && role.getRelStoreId() != null) {
                userExt.managedStoreIds.add(role.getRelStoreId());
            }
            if (!ESystemRole.isDefaultRole(role.getRoleId()) && role.getStoreId() != null) {
                userExt.subStoreIds.add(role.getStoreId());
            }
        }
        for (SysMenu menu : CollUtil.emptyIfNull(userInfo.getMenus())) {
            userExt.menuIds.add(menu.getMenuId());
            if (StrUtil.isNotEmpty(menu.getPerms())) {
                userExt.menuPerms.add(menu.getPerms());
            }
        }
        return userExt;
    }

}
