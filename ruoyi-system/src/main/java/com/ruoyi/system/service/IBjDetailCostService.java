package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjDetailCost;

/**
 * costService接口
 * 
 * @author ssq
 * @date 2024-10-04
 */
public interface IBjDetailCostService 
{
    /**
     * 查询cost
     * 
     * @param id cost主键
     * @return cost
     */
    public BjDetailCost selectBjDetailCostById(Long id);

    /**
     * 查询cost列表
     * 
     * @param bjDetailCost cost
     * @return cost集合
     */
    public List<BjDetailCost> selectBjDetailCostList(BjDetailCost bjDetailCost);

    /**
     * 新增cost
     * 
     * @param bjDetailCost cost
     * @return 结果
     */
    public int insertBjDetailCost(BjDetailCost bjDetailCost);

    /**
     * 修改cost
     * 
     * @param bjDetailCost cost
     * @return 结果
     */
    public int updateBjDetailCost(BjDetailCost bjDetailCost);

    /**
     * 批量删除cost
     * 
     * @param ids 需要删除的cost主键集合
     * @return 结果
     */
    public int deleteBjDetailCostByIds(Long[] ids);

    /**
     * 删除cost信息
     * 
     * @param id cost主键
     * @return 结果
     */
    public int deleteBjDetailCostById(Long id);
}
