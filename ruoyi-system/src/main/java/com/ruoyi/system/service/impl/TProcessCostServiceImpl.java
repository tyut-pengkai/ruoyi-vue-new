package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TProcessCostMapper;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.service.ITProcessCostService;

/**
 * 加工费用Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class TProcessCostServiceImpl implements ITProcessCostService 
{
    @Autowired
    private TProcessCostMapper tProcessCostMapper;

    /**
     * 查询加工费用
     * 
     * @param id 加工费用主键
     * @return 加工费用
     */
    @Override
    public TProcessCost selectTProcessCostById(Long id)
    {
        return tProcessCostMapper.selectTProcessCostById(id);
    }

    /**
     * 查询加工费用列表
     * 
     * @param tProcessCost 加工费用
     * @return 加工费用
     */
    @Override
    public List<TProcessCost> selectTProcessCostList(TProcessCost tProcessCost)
    {
        return tProcessCostMapper.selectTProcessCostList(tProcessCost);
    }

    /**
     * 新增加工费用
     * 
     * @param tProcessCost 加工费用
     * @return 结果
     */
    @Override
    public int insertTProcessCost(TProcessCost tProcessCost)
    {
        return tProcessCostMapper.insertTProcessCost(tProcessCost);
    }

    /**
     * 修改加工费用
     * 
     * @param tProcessCost 加工费用
     * @return 结果
     */
    @Override
    public int updateTProcessCost(TProcessCost tProcessCost)
    {
        return tProcessCostMapper.updateTProcessCost(tProcessCost);
    }

    /**
     * 批量删除加工费用
     * 
     * @param ids 需要删除的加工费用主键
     * @return 结果
     */
    @Override
    public int deleteTProcessCostByIds(Long[] ids)
    {
        return tProcessCostMapper.deleteTProcessCostByIds(ids);
    }

    /**
     * 删除加工费用信息
     * 
     * @param id 加工费用主键
     * @return 结果
     */
    @Override
    public int deleteTProcessCostById(Long id)
    {
        return tProcessCostMapper.deleteTProcessCostById(id);
    }
}
