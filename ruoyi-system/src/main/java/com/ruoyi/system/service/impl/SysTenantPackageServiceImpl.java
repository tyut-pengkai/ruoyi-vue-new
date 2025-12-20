package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysTenantPackageMapper;
import com.ruoyi.system.domain.SysTenantPackage;
import com.ruoyi.system.service.ISysTenantPackageService;

/**
 * 租户套餐Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-19
 */
@Service
public class SysTenantPackageServiceImpl implements ISysTenantPackageService
{
    @Autowired
    private SysTenantPackageMapper tenantPackageMapper;

    /**
     * 查询租户套餐
     *
     * @param packageId 租户套餐主键
     * @return 租户套餐
     */
    @Override
    public SysTenantPackage selectPackageById(Long packageId)
    {
        return tenantPackageMapper.selectPackageById(packageId);
    }

    /**
     * 查询租户套餐列表
     *
     * @param sysTenantPackage 租户套餐
     * @return 租户套餐
     */
    @Override
    public List<SysTenantPackage> selectPackageList(SysTenantPackage sysTenantPackage)
    {
        return tenantPackageMapper.selectPackageList(sysTenantPackage);
    }

    /**
     * 新增租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPackage(SysTenantPackage sysTenantPackage)
    {
        sysTenantPackage.setCreateTime(DateUtils.getNowDate());
        int rows = tenantPackageMapper.insertPackage(sysTenantPackage);
        // 保存菜单关联
        if (sysTenantPackage.getMenuIds() != null && sysTenantPackage.getMenuIds().length > 0)
        {
            tenantPackageMapper.batchInsertPackageMenu(sysTenantPackage.getPackageId(), sysTenantPackage.getMenuIds());
        }
        return rows;
    }

    /**
     * 修改租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePackage(SysTenantPackage sysTenantPackage)
    {
        sysTenantPackage.setUpdateTime(DateUtils.getNowDate());
        // 先删除旧的菜单关联
        tenantPackageMapper.deletePackageMenuByPackageId(sysTenantPackage.getPackageId());
        // 再插入新的菜单关联
        if (sysTenantPackage.getMenuIds() != null && sysTenantPackage.getMenuIds().length > 0)
        {
            tenantPackageMapper.batchInsertPackageMenu(sysTenantPackage.getPackageId(), sysTenantPackage.getMenuIds());
        }
        return tenantPackageMapper.updatePackage(sysTenantPackage);
    }

    /**
     * 批量删除租户套餐
     *
     * @param packageIds 需要删除的租户套餐主键
     * @return 结果
     */
    @Override
    public int deletePackageByIds(Long[] packageIds)
    {
        return tenantPackageMapper.deletePackageByIds(packageIds);
    }

    /**
     * 删除租户套餐信息
     *
     * @param packageId 租户套餐主键
     * @return 结果
     */
    @Override
    public int deletePackageById(Long packageId)
    {
        return tenantPackageMapper.deletePackageById(packageId);
    }

    /**
     * 查询套餐包含的菜单ID列表
     *
     * @param packageId 套餐ID
     * @return 菜单ID列表，空列表表示高级套餐（不限制）
     */
    @Override
    public List<Long> selectMenuIdsByPackageId(Long packageId)
    {
        return tenantPackageMapper.selectMenuIdsByPackageId(packageId);
    }
}
