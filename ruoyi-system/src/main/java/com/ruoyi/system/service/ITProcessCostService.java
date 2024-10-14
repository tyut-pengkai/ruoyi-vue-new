package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TProcessCost;

/**
 * 加工费用Service接口
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public interface ITProcessCostService 
{
    /**
     * 查询加工费用
     * 
     * @param id 加工费用主键
     * @return 加工费用
     */
    public TProcessCost selectTProcessCostById(Long id);

    /**
     * 查询加工费用列表
     * 
     * @param tProcessCost 加工费用
     * @return 加工费用集合
     */
    public List<TProcessCost> selectTProcessCostList(TProcessCost tProcessCost);

    /**
     * 新增加工费用
     * 
     * @param tProcessCost 加工费用
     * @return 结果
     */
    public int insertTProcessCost(TProcessCost tProcessCost);

    /**
     * 修改加工费用
     * 
     * @param tProcessCost 加工费用
     * @return 结果
     */
    public int updateTProcessCost(TProcessCost tProcessCost);

    /**
     * 批量删除加工费用
     * 
     * @param ids 需要删除的加工费用主键集合
     * @return 结果
     */
    public int deleteTProcessCostByIds(Long[] ids);

    /**
     * 删除加工费用信息
     * 
     * @param id 加工费用主键
     * @return 结果
     */
    public int deleteTProcessCostById(Long id);
}
