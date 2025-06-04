package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    final SysRoleMapper roleMapper;
    final SysRoleMenuMapper roleMenuMapper;
    final SysUserRoleMapper userRoleMapper;

    @Override
    public RoleInfo getRoleById(Long roleId) {
        return roleMapper.getRoleInfoById(roleId);
    }

    @Override
    public List<RoleListItem> listRole(RoleQuery query) {
        return roleMapper.listRole(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createRole(RoleInfoEdit roleEdit) {
        // 新增角色
        SysRole role = BeanUtil.toBean(roleEdit, SysRole.class);
        insertRoleBase(role);
        roleEdit.setRoleId(role.getRoleId());
        // 关联角色菜单
        insertRoleMenu(roleEdit);
        return roleEdit.getRoleId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope updateRole(RoleInfoEdit roleEdit) {
        // 修改角色
        Assert.notNull(roleEdit.getRoleId());
        SysRole role = roleMapper.selectById(roleEdit.getRoleId());
        Assert.notNull(role);
        role.setRoleName(roleEdit.getRoleName());
        role.setRoleKey(roleEdit.getRoleKey());
        role.setRoleSort(roleEdit.getRoleSort());
        role.setStoreId(roleEdit.getStoreId());
        List<Long> userIds = ListUtil.empty();
        if (!Constants.SYS_NORMAL_STATUS.equals(roleEdit.getStatus())) {
            checkRoleDeleteAccess(role);
            userIds = userRoleMapper.listRelUserId(roleEdit.getRoleId());
        }
        role.setStatus(roleEdit.getStatus());
        role.setRemark(roleEdit.getRemark());
        updateRoleBase(role);
        // 删除角色菜单
        roleMenuMapper.deleteRoleMenuByRoleId(roleEdit.getRoleId());
        // 关联角色菜单
        insertRoleMenu(roleEdit);
        return new InfluenceScope(1, new HashSet<>(userIds));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope batchUpdateStatus(List<Long> roleIds, String status) {
        int c = 0;
        Set<Long> userIds = new HashSet<>();
        if (CollUtil.isNotEmpty(roleIds) && StrUtil.isNotEmpty(status)) {
            List<SysRole> roles = roleMapper.selectByIds(roleIds);
            for (SysRole role : roles) {
                if (!Constants.SYS_NORMAL_STATUS.equals(status)) {
                    checkRoleDeleteAccess(role);
                    userIds.addAll(userRoleMapper.listRelUserId(role.getRoleId()));
                }
                if (!status.equals(role.getStatus())) {
                    role.setStatus(status);
                    updateRoleBase(role);
                    c++;
                }
            }
        }
        return new InfluenceScope(c, userIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope batchDelete(List<Long> roleIds) {
        int c = 0;
        Set<Long> userIds = new HashSet<>();
        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysRole> roles = roleMapper.selectByIds(roleIds);
            for (SysRole role : roles) {
                if (Constants.UNDELETED.equals(role.getDelFlag())) {
                    checkRoleDeleteAccess(role);
                    userIds.addAll(userRoleMapper.listRelUserId(role.getRoleId()));
                    role.setDelFlag(Constants.DELETED);
                    updateRoleBase(role);
                    // 删除角色与菜单关联
                    roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
                    c++;
                }
            }
        }
        return new InfluenceScope(c, userIds);
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<RoleListItem> listAllRole() {
        return SpringUtils.getAopProxy(this).listRole(new RoleQuery());
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    @Override
    public List<RoleSelectItem> listRoleSelectItem(Long userId) {
        return userRoleMapper.listRoleSelectItem(userId);
    }

    /**
     * 新增角色菜单信息
     *
     * @param roleEdit 角色对象
     */
    private void insertRoleMenu(RoleInfoEdit roleEdit) {
        // 新增用户与角色管理
        if (CollUtil.isNotEmpty(roleEdit.getMenuIds())) {
            List<SysRoleMenu> list = new ArrayList<>(roleEdit.getMenuIds().size());
            for (Long menuId : roleEdit.getMenuIds()) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleEdit.getRoleId());
                rm.setMenuId(menuId);
                list.add(rm);
            }
            roleMenuMapper.batchRoleMenu(list);
        }
    }

    /**
     * 新增角色
     *
     * @param role
     */
    private void insertRoleBase(SysRole role) {
        checkRoleBase(role);
        Assert.isNull(role.getRoleId());
        if (role.getRoleSort() == null) {
            role.setRoleSort(1);
        }
        role.setStatus(Constants.SYS_NORMAL_STATUS);
        role.setDelFlag(Constants.UNDELETED);
        role.setVersion(0L);
        roleMapper.insert(role);
    }


    /**
     * 修改角色
     *
     * @param role
     */
    private void updateRoleBase(SysRole role) {
        checkRoleBase(role);
        Assert.notNull(role.getRoleId());
        int c = roleMapper.updateById(role);
        if (c == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
    }

    /**
     * 角色基础信息校验
     *
     * @param role
     */
    private void checkRoleBase(SysRole role) {
        Assert.notNull(role);
        Assert.notEmpty(role.getRoleName(), "角色名称不能为空");
        Assert.notEmpty(role.getRoleKey(), "角色权限不能为空");
    }

    /**
     * 检查是否可删除/停用
     *
     * @param role
     */
    private void checkRoleDeleteAccess(SysRole role) {
        if (ESystemRole.isDefaultRole(role.getRoleId())) {
            throw new ServiceException("默认角色[" + role.getRoleName() + "]无法停用或删除");
        }
        int useCount = countUserRoleByRoleId(role.getRoleId());
        if (useCount > 0) {
            throw new ServiceException("角色[" + role.getRoleName() + "]使用中，无法停用或删除");
        }
    }

}
