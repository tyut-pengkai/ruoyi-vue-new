package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysTenantPackage;
import org.apache.ibatis.annotations.Param;

/**
 * 租户套餐Mapper接口
 *
 * @author ruoyi
 * @date 2025-12-19
 */
public interface SysTenantPackageMapper
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
     * 删除租户套餐
     *
     * @param packageId 租户套餐主键
     * @return 结果
     */
    public int deletePackageById(Long packageId);

    /**
     * 批量删除租户套餐
     *
     * @param packageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePackageByIds(Long[] packageIds);

    /**
     * 查询套餐包含的菜单ID列表
     *
     * @param packageId 套餐ID
     * @return 菜单ID列表
     */
    public List<Long> selectMenuIdsByPackageId(Long packageId);

    /**
     * 批量新增套餐菜单关联
     *
     * @param packageId 套餐ID
     * @param menuIds 菜单ID列表
     * @return 结果
     */
    public int batchInsertPackageMenu(@Param("packageId") Long packageId, @Param("menuIds") Long[] menuIds);

    /**
     * 删除套餐菜单关联
     *
     * @param packageId 套餐ID
     * @return 结果
     */
    public int deletePackageMenuByPackageId(Long packageId);
}
