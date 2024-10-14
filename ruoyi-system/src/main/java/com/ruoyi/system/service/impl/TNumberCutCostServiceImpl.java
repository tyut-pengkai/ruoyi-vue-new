package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TNumberCutCostMapper;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.service.ITNumberCutCostService;

/**
 * 数割费用Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class TNumberCutCostServiceImpl implements ITNumberCutCostService 
{
    @Autowired
    private TNumberCutCostMapper tNumberCutCostMapper;

    /**
     * 查询数割费用
     * 
     * @param id 数割费用主键
     * @return 数割费用
     */
    @Override
    public TNumberCutCost selectTNumberCutCostById(Long id)
    {
        return tNumberCutCostMapper.selectTNumberCutCostById(id);
    }

    /**
     * 查询数割费用列表
     * 
     * @param tNumberCutCost 数割费用
     * @return 数割费用
     */
    @Override
    public List<TNumberCutCost> selectTNumberCutCostList(TNumberCutCost tNumberCutCost)
    {
        return tNumberCutCostMapper.selectTNumberCutCostList(tNumberCutCost);
    }

    /**
     * 新增数割费用
     * 
     * @param tNumberCutCost 数割费用
     * @return 结果
     */
    @Override
    public int insertTNumberCutCost(TNumberCutCost tNumberCutCost)
    {
        return tNumberCutCostMapper.insertTNumberCutCost(tNumberCutCost);
    }

    /**
     * 修改数割费用
     * 
     * @param tNumberCutCost 数割费用
     * @return 结果
     */
    @Override
    public int updateTNumberCutCost(TNumberCutCost tNumberCutCost)
    {
        return tNumberCutCostMapper.updateTNumberCutCost(tNumberCutCost);
    }

    /**
     * 批量删除数割费用
     * 
     * @param ids 需要删除的数割费用主键
     * @return 结果
     */
    @Override
    public int deleteTNumberCutCostByIds(Long[] ids)
    {
        return tNumberCutCostMapper.deleteTNumberCutCostByIds(ids);
    }

    /**
     * 删除数割费用信息
     * 
     * @param id 数割费用主键
     * @return 结果
     */
    @Override
    public int deleteTNumberCutCostById(Long id)
    {
        return tNumberCutCostMapper.deleteTNumberCutCostById(id);
    }
}
