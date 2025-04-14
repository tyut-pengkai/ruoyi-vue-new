package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.dto.role.*;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

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

    /**
     * 新增角色
     *
     * @param roleDTO 新增角色入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(RoleDTO roleDTO) {
        if (ObjectUtils.isNotEmpty(roleDTO.getRoleId())) {
            throw new ServiceException("新增角色失败，roleId必须为空!", HttpStatus.ERROR);
        }
        if (!this.checkRoleNameUnique(roleDTO.getRoleId(), roleDTO.getRoleName())) {
            throw new ServiceException("新增角色'" + roleDTO.getRoleName() + "'失败，角色名称已存在", HttpStatus.ERROR);
        } else if (!this.checkRoleKeyUnique(roleDTO.getRoleId(), roleDTO.getRoleKey())) {
            throw new ServiceException("新增角色'" + roleDTO.getRoleName() + "'失败，角色权限已存在", HttpStatus.ERROR);
        }
        SysRole role = BeanUtil.toBean(roleDTO, SysRole.class);
        role.setCreateBy(getUsername());
        int count = this.roleMapper.insert(role);
        // 新增用户与角色管理
        List<SysRoleMenu> list = roleDTO.getMenuIdList().stream().map(menuId -> new SysRoleMenu() {{
            setRoleId(role.getRoleId());
            setMenuId(menuId);
        }}).collect(Collectors.toList());
        this.roleMenuMapper.insert(list);
        return count;
    }

    /**
     * 编辑角色
     *
     * @param roleDTO 编辑角色入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(RoleUpdateDTO updateDTO) {
        // 超级管理员角色不可编辑
        this.checkRoleAllowed(updateDTO.getRoleId());
        if (!this.checkRoleNameUnique(updateDTO.getRoleId(), updateDTO.getRoleName())) {
            throw new ServiceException("修改角色'" + updateDTO.getRoleName() + "'失败，角色名称已存在", HttpStatus.ERROR);
        } else if (!this.checkRoleKeyUnique(updateDTO.getRoleId(), updateDTO.getRoleKey())) {
            throw new ServiceException("修改角色'" + updateDTO.getRoleName() + "'失败，角色权限已存在", HttpStatus.ERROR);
        }
        SysRole role = Optional.ofNullable(this.roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleId, updateDTO.getRoleId()).eq(SysRole::getDelFlag, Constants.UNDELETED)))
                        .orElseThrow(() -> new ServiceException("角色不存在!", HttpStatus.ERROR));
        role.setUpdateBy(getUsername());
        BeanUtil.copyProperties(updateDTO, role);
       int count = this.roleMapper.updateById(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        // 新增用户与角色管理
        List<SysRoleMenu> list = updateDTO.getMenuIdList().stream().map(menuId -> new SysRoleMenu() {{
            setRoleId(role.getRoleId());
            setMenuId(menuId);
        }}).collect(Collectors.toList());
        this.roleMenuMapper.insert(list);
        return count;
    }

    /**
     * 更新角色状态
     *
     * @param statusDTO 更新角色状态入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStatus(RoleUpdateStatusDTO statusDTO) {
        this.checkRoleAllowed(statusDTO.getRoleId());
        SysRole role = Optional.ofNullable(this.roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleId, statusDTO.getRoleId()).eq(SysRole::getDelFlag, Constants.UNDELETED)))
                        .orElseThrow(() -> new ServiceException("角色不存在!", HttpStatus.ERROR));
        role.setStatus(statusDTO.getStatus());
        role.setUpdateBy(getUsername());
        return this.roleMapper.updateById(role);
    }

    /**
     * 删除角色
     *
     * @param deleteDTO 删除角色ID列表
     * @return Integer
     */
    @Override
    @Transactional
    public Integer batchRemove(RoleDeleteDTO deleteDTO) {
        for (Long roleId : deleteDTO.getRoleIdList()) {
            checkRoleAllowed(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 将list 转为 数组
        Long[] roleIds = deleteDTO.getRoleIdList().toArray(new Long[0]);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 获取角色详情
     *
     * @param roleId 角色ID
     * @return RoleResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public RoleResDTO getRoleInfo(Long roleId) {
        SysRole role = Optional.ofNullable(this.roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleId, roleId).eq(SysRole::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("角色不存在!"));
        return BeanUtil.toBean(role, RoleResDTO.class);
    }

    /**
     * 获取角色分页
     *
     * @param pageDTO 角色分页入参
     * @return Page<RolePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RolePageResDTO> page(RolePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getDelFlag, Constants.UNDELETED);
        if (StringUtils.isNotBlank(pageDTO.getRoleName())) {
            queryWrapper.like(SysRole::getRoleName, pageDTO.getRoleName());
        }
        if (StringUtils.isNotBlank(pageDTO.getRoleKey())) {
            queryWrapper.like(SysRole::getRoleKey, pageDTO.getRoleKey());
        }
        if (StringUtils.isNotBlank(pageDTO.getStatus())) {
            queryWrapper.eq(SysRole::getStatus, pageDTO.getStatus());
        }
        List<SysRole> list = this.roleMapper.selectList(queryWrapper);
        return Page.convert(new PageInfo<>(list), BeanUtil.copyToList(list, RolePageResDTO.class));
    }


    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    private void checkRoleAllowed(Long roleId) {
        if (StringUtils.isNotNull(roleId) && SysRole.isAdmin(roleId)) {
            throw new ServiceException("不允许操作超级管理员角色", HttpStatus.ERROR);
        }
    }


    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    private boolean checkRoleKeyUnique(Long id, String roleKey) {
        Long roleId = ObjectUtils.defaultIfNull(id, -1L);
        SysRole info = roleMapper.checkRoleKeyUnique(roleKey);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }



    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    private boolean checkRoleNameUnique(Long id, String roleName) {
        Long roleId = ObjectUtils.defaultIfNull(id, -1L);
        SysRole info = roleMapper.checkRoleNameUnique(roleName);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }




    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    // =============================================================================================
    // =============================================================================================
    // =============================================================================================
    // =============================================================================================











    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色id
     */
    @Override
    public void checkRoleDataScope(Long... roleIds) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            for (Long roleId : roleIds) {
                SysRole role = new SysRole();
                role.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                if (StringUtils.isEmpty(roles)) {
                    throw new ServiceException("没有权限访问角色数据！");
                }
            }
        }
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

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        return roleMapper.updateRole(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
       /* // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }*/
        return rows;
    }


    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(roleId);
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

}
