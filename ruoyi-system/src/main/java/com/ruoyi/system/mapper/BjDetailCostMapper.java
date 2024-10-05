package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BjDetailCost;

/**
 * costMapper接口
 * 
 * @author ssq
 * @date 2024-10-04
 */
public interface BjDetailCostMapper 
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
     * 删除cost
     * 
     * @param id cost主键
     * @return 结果
     */
    public int deleteBjDetailCostById(Long id);

    /**
     * 批量删除cost
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBjDetailCostByIds(Long[] ids);
}
