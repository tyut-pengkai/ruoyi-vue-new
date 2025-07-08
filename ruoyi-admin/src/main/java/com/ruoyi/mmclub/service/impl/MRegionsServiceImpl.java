package com.ruoyi.mmclub.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mmclub.mapper.MRegionsMapper;
import com.ruoyi.mmclub.domain.MRegions;
import com.ruoyi.mmclub.service.IMRegionsService;

/**
 * 地区管理Service业务层处理
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@Service
public class MRegionsServiceImpl implements IMRegionsService 
{
    @Autowired
    private MRegionsMapper mRegionsMapper;

    /**
     * 查询地区管理
     * 
     * @param id 地区管理主键
     * @return 地区管理
     */
    @Override
    public MRegions selectMRegionsById(Long id)
    {
        return mRegionsMapper.selectMRegionsById(id);
    }

    /**
     * 查询地区管理列表
     * 
     * @param mRegions 地区管理
     * @return 地区管理
     */
    @Override
    public List<MRegions> selectMRegionsList(MRegions mRegions)
    {
        return mRegionsMapper.selectMRegionsList(mRegions);
    }

    /**
     * 新增地区管理
     * 
     * @param mRegions 地区管理
     * @return 结果
     */
    @Override
    public int insertMRegions(MRegions mRegions)
    {
        mRegions.setCreateTime(DateUtils.getNowDate());
        return mRegionsMapper.insertMRegions(mRegions);
    }

    /**
     * 修改地区管理
     * 
     * @param mRegions 地区管理
     * @return 结果
     */
    @Override
    public int updateMRegions(MRegions mRegions)
    {
        mRegions.setUpdateTime(DateUtils.getNowDate());
        return mRegionsMapper.updateMRegions(mRegions);
    }

    /**
     * 批量删除地区管理
     * 
     * @param ids 需要删除的地区管理主键
     * @return 结果
     */
    @Override
    public int deleteMRegionsByIds(Long[] ids)
    {
        return mRegionsMapper.deleteMRegionsByIds(ids);
    }

    /**
     * 删除地区管理信息
     * 
     * @param id 地区管理主键
     * @return 结果
     */
    @Override
    public int deleteMRegionsById(Long id)
    {
        return mRegionsMapper.deleteMRegionsById(id);
    }
}
