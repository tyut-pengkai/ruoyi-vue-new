package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysTenantPackage;

/**
 * 租户套餐Service接口
 *
 * @author ruoyi
 * @date 2025-12-19
 */
public interface ISysTenantPackageService
{
    /**
     * 查询租户套餐
     *
     * @param packageId 租户套餐主键
     * @return 租户套餐
     */
    public SysTenantPackage selectPackageById(Long packageId);

    /**
     * 查询租户套餐列表
     *
     * @param sysTenantPackage 租户套餐
     * @return 租户套餐集合
     */
    public List<SysTenantPackage> selectPackageList(SysTenantPackage sysTenantPackage);

    /**
     * 新增租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    public int insertPackage(SysTenantPackage sysTenantPackage);

    /**
     * 修改租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    public int updatePackage(SysTenantPackage sysTenantPackage);

    /**
     * 批量删除租户套餐
     *
     * @param packageIds 需要删除的租户套餐主键集合
     * @return 结果
     */
    public int deletePackageByIds(Long[] packageIds);

    /**
     * 删除租户套餐信息
     *
     * @param packageId 租户套餐主键
     * @return 结果
     */
    public int deletePackageById(Long packageId);

    /**
     * 查询套餐包含的菜单ID列表
     *
     * Reason: 用于套餐菜单过滤，返回空列表表示不限制
     *
     * @param packageId 套餐ID
     * @return 菜单ID列表，空列表表示高级套餐（不限制）
     */
    public List<Long> selectMenuIdsByPackageId(Long packageId);
}
