package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysTenant;
import com.ruoyi.system.mapper.SysTenantMapper;
import com.ruoyi.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户信息Service业务层处理
 * 
 * @author W-yf
 * @date 2025-12-19
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService
{
    @Autowired
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询租户信息
     * 
     * @param tenantId 租户信息主键
     * @return 租户信息
     */
    @Override
    public SysTenant selectSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.selectSysTenantByTenantId(tenantId);
    }

    /**
     * 查询租户信息列表
     * 
     * @param sysTenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant)
    {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户信息
     * 
     * @param sysTenant 租户信息
     * @return 结果
     */
    @Override
    public int insertSysTenant(SysTenant sysTenant)
    {
        sysTenant.setCreateTime(DateUtils.getNowDate());
        return sysTenantMapper.insertSysTenant(sysTenant);
    }

    /**
     * 修改租户信息
     * 
     * @param sysTenant 租户信息
     * @return 结果
     */
    @Override
    public int updateSysTenant(SysTenant sysTenant)
    {
        sysTenant.setUpdateTime(DateUtils.getNowDate());
        return sysTenantMapper.updateSysTenant(sysTenant);
    }

    /**
     * 批量删除租户信息
     * 
     * @param tenantIds 需要删除的租户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantIds(Long[] tenantIds)
    {
        return sysTenantMapper.deleteSysTenantByTenantIds(tenantIds);
    }

    /**
     * 删除租户信息信息
     * 
     * @param tenantId 租户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.deleteSysTenantByTenantId(tenantId);
    }
}
