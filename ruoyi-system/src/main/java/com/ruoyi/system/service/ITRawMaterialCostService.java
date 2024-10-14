package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TRawMaterialCost;

/**
 * 原材料费用Service接口
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public interface ITRawMaterialCostService 
{
    /**
     * 查询原材料费用
     * 
     * @param id 原材料费用主键
     * @return 原材料费用
     */
    public TRawMaterialCost selectTRawMaterialCostById(Long id);

    /**
     * 查询原材料费用列表
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 原材料费用集合
     */
    public List<TRawMaterialCost> selectTRawMaterialCostList(TRawMaterialCost tRawMaterialCost);

    /**
     * 新增原材料费用
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 结果
     */
    public int insertTRawMaterialCost(TRawMaterialCost tRawMaterialCost);

    /**
     * 修改原材料费用
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 结果
     */
    public int updateTRawMaterialCost(TRawMaterialCost tRawMaterialCost);

    /**
     * 批量删除原材料费用
     * 
     * @param ids 需要删除的原材料费用主键集合
     * @return 结果
     */
    public int deleteTRawMaterialCostByIds(Long[] ids);

    /**
     * 删除原材料费用信息
     * 
     * @param id 原材料费用主键
     * @return 结果
     */
    public int deleteTRawMaterialCostById(Long id);
}
