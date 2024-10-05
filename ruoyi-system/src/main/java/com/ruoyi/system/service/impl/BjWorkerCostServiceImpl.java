package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjWorkerCostMapper;
import com.ruoyi.system.domain.BjWorkerCost;
import com.ruoyi.system.service.IBjWorkerCostService;

/**
 * 人工成本Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjWorkerCostServiceImpl implements IBjWorkerCostService 
{
    @Autowired
    private BjWorkerCostMapper bjWorkerCostMapper;

    /**
     * 查询人工成本
     * 
     * @param id 人工成本主键
     * @return 人工成本
     */
    @Override
    public BjWorkerCost selectBjWorkerCostById(Long id)
    {
        return bjWorkerCostMapper.selectBjWorkerCostById(id);
    }

    /**
     * 查询人工成本列表
     * 
     * @param bjWorkerCost 人工成本
     * @return 人工成本
     */
    @Override
    public List<BjWorkerCost> selectBjWorkerCostList(BjWorkerCost bjWorkerCost)
    {
        return bjWorkerCostMapper.selectBjWorkerCostList(bjWorkerCost);
    }

    /**
     * 新增人工成本
     * 
     * @param bjWorkerCost 人工成本
     * @return 结果
     */
    @Override
    public int insertBjWorkerCost(BjWorkerCost bjWorkerCost)
    {
        return bjWorkerCostMapper.insertBjWorkerCost(bjWorkerCost);
    }

    /**
     * 修改人工成本
     * 
     * @param bjWorkerCost 人工成本
     * @return 结果
     */
    @Override
    public int updateBjWorkerCost(BjWorkerCost bjWorkerCost)
    {
        return bjWorkerCostMapper.updateBjWorkerCost(bjWorkerCost);
    }

    /**
     * 批量删除人工成本
     * 
     * @param ids 需要删除的人工成本主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkerCostByIds(Long[] ids)
    {
        return bjWorkerCostMapper.deleteBjWorkerCostByIds(ids);
    }

    /**
     * 删除人工成本信息
     * 
     * @param id 人工成本主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkerCostById(Long id)
    {
        return bjWorkerCostMapper.deleteBjWorkerCostById(id);
    }
}
