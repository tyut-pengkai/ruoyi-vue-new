package com.ruoyi.mmclub.mapper;

import java.util.List;
import com.ruoyi.mmclub.domain.MRegions;

/**
 * 地区管理Mapper接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface MRegionsMapper 
{
    /**
     * 查询地区管理
     * 
     * @param id 地区管理主键
     * @return 地区管理
     */
    public MRegions selectMRegionsById(Long id);

    /**
     * 查询地区管理列表
     * 
     * @param mRegions 地区管理
     * @return 地区管理集合
     */
    public List<MRegions> selectMRegionsList(MRegions mRegions);

    /**
     * 新增地区管理
     * 
     * @param mRegions 地区管理
     * @return 结果
     */
    public int insertMRegions(MRegions mRegions);

    /**
     * 修改地区管理
     * 
     * @param mRegions 地区管理
     * @return 结果
     */
    public int updateMRegions(MRegions mRegions);

    /**
     * 删除地区管理
     * 
     * @param id 地区管理主键
     * @return 结果
     */
    public int deleteMRegionsById(Long id);

    /**
     * 批量删除地区管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMRegionsByIds(Long[] ids);
}
