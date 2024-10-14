package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TRawMaterialCostMapper;
import com.ruoyi.system.domain.TRawMaterialCost;
import com.ruoyi.system.service.ITRawMaterialCostService;

/**
 * 原材料费用Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class TRawMaterialCostServiceImpl implements ITRawMaterialCostService 
{
    @Autowired
    private TRawMaterialCostMapper tRawMaterialCostMapper;

    /**
     * 查询原材料费用
     * 
     * @param id 原材料费用主键
     * @return 原材料费用
     */
    @Override
    public TRawMaterialCost selectTRawMaterialCostById(Long id)
    {
        return tRawMaterialCostMapper.selectTRawMaterialCostById(id);
    }

    /**
     * 查询原材料费用列表
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 原材料费用
     */
    @Override
    public List<TRawMaterialCost> selectTRawMaterialCostList(TRawMaterialCost tRawMaterialCost)
    {
        return tRawMaterialCostMapper.selectTRawMaterialCostList(tRawMaterialCost);
    }

    /**
     * 新增原材料费用
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 结果
     */
    @Override
    public int insertTRawMaterialCost(TRawMaterialCost tRawMaterialCost)
    {
        return tRawMaterialCostMapper.insertTRawMaterialCost(tRawMaterialCost);
    }

    /**
     * 修改原材料费用
     * 
     * @param tRawMaterialCost 原材料费用
     * @return 结果
     */
    @Override
    public int updateTRawMaterialCost(TRawMaterialCost tRawMaterialCost)
    {
        return tRawMaterialCostMapper.updateTRawMaterialCost(tRawMaterialCost);
    }

    /**
     * 批量删除原材料费用
     * 
     * @param ids 需要删除的原材料费用主键
     * @return 结果
     */
    @Override
    public int deleteTRawMaterialCostByIds(Long[] ids)
    {
        return tRawMaterialCostMapper.deleteTRawMaterialCostByIds(ids);
    }

    /**
     * 删除原材料费用信息
     * 
     * @param id 原材料费用主键
     * @return 结果
     */
    @Override
    public int deleteTRawMaterialCostById(Long id)
    {
        return tRawMaterialCostMapper.deleteTRawMaterialCostById(id);
    }
}
