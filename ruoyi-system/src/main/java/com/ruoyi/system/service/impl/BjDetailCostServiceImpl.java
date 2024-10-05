package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjDetailCostMapper;
import com.ruoyi.system.domain.BjDetailCost;
import com.ruoyi.system.service.IBjDetailCostService;

/**
 * costService业务层处理
 * 
 * @author ssq
 * @date 2024-10-04
 */
@Service
public class BjDetailCostServiceImpl implements IBjDetailCostService 
{
    @Autowired
    private BjDetailCostMapper bjDetailCostMapper;

    /**
     * 查询cost
     * 
     * @param id cost主键
     * @return cost
     */
    @Override
    public BjDetailCost selectBjDetailCostById(Long id)
    {
        return bjDetailCostMapper.selectBjDetailCostById(id);
    }

    /**
     * 查询cost列表
     * 
     * @param bjDetailCost cost
     * @return cost
     */
    @Override
    public List<BjDetailCost> selectBjDetailCostList(BjDetailCost bjDetailCost)
    {
        return bjDetailCostMapper.selectBjDetailCostList(bjDetailCost);
    }

    /**
     * 新增cost
     * 
     * @param bjDetailCost cost
     * @return 结果
     */
    @Override
    public int insertBjDetailCost(BjDetailCost bjDetailCost)
    {
        return bjDetailCostMapper.insertBjDetailCost(bjDetailCost);
    }

    /**
     * 修改cost
     * 
     * @param bjDetailCost cost
     * @return 结果
     */
    @Override
    public int updateBjDetailCost(BjDetailCost bjDetailCost)
    {
        return bjDetailCostMapper.updateBjDetailCost(bjDetailCost);
    }

    /**
     * 批量删除cost
     * 
     * @param ids 需要删除的cost主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailCostByIds(Long[] ids)
    {
        return bjDetailCostMapper.deleteBjDetailCostByIds(ids);
    }

    /**
     * 删除cost信息
     * 
     * @param id cost主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailCostById(Long id)
    {
        return bjDetailCostMapper.deleteBjDetailCostById(id);
    }
}
