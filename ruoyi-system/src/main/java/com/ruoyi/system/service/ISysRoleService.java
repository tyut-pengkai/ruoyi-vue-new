package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.model.*;

import java.util.List;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
public interface ISysRoleService {
    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    RoleInfo getRoleById(Long roleId);

    /**
     * 角色列表
     *
     * @param query
     * @return
     */
    List<RoleListItem> listRole(RoleQuery query);

    /**
     * 创建角色
     *
     * @param roleEdit
     * @return
     */
    Long createRole(RoleInfoEdit roleEdit);

    /**
     * 修改角色
     *
     * @param roleEdit
     * @return
     */
    InfluenceScope updateRole(RoleInfoEdit roleEdit);

    /**
     * 批量更新状态
     *
     * @param roleIds
     * @param status
     * @return
     */
    InfluenceScope batchUpdateStatus(List<Long> roleIds, String status);

    /**
     * 批量删除
     *
     * @param roleIds
     * @return
     */
    InfluenceScope batchDelete(List<Long> roleIds);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<RoleListItem> listAllRole();

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 获取角色选择列表
     *
     * @param userId
     * @return
     */
    List<RoleSelectItem> listRoleSelectItem(Long userId);
}
