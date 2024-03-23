package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysRoleDept;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与分组关联表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysRoleDeptMapper
{
    /**
     * 通过角色ID删除角色和分组关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量删除角色分组关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * 查询分组使用数量
     *
     * @param deptId 分组ID
     * @return 结果
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * 批量新增角色分组信息
     *
     * @param roleDeptList 角色分组列表
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
