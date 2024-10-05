package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjParamConfigCostMapper;
import com.ruoyi.system.domain.BjParamConfigCost;
import com.ruoyi.system.service.IBjParamConfigCostService;

/**
 * 成本参数Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjParamConfigCostServiceImpl implements IBjParamConfigCostService 
{
    @Autowired
    private BjParamConfigCostMapper bjParamConfigCostMapper;

    /**
     * 查询成本参数
     * 
     * @param id 成本参数主键
     * @return 成本参数
     */
    @Override
    public BjParamConfigCost selectBjParamConfigCostById(Long id)
    {
        return bjParamConfigCostMapper.selectBjParamConfigCostById(id);
    }

    /**
     * 查询成本参数列表
     * 
     * @param bjParamConfigCost 成本参数
     * @return 成本参数
     */
    @Override
    public List<BjParamConfigCost> selectBjParamConfigCostList(BjParamConfigCost bjParamConfigCost)
    {
        return bjParamConfigCostMapper.selectBjParamConfigCostList(bjParamConfigCost);
    }

    /**
     * 新增成本参数
     * 
     * @param bjParamConfigCost 成本参数
     * @return 结果
     */
    @Override
    public int insertBjParamConfigCost(BjParamConfigCost bjParamConfigCost)
    {
        return bjParamConfigCostMapper.insertBjParamConfigCost(bjParamConfigCost);
    }

    /**
     * 修改成本参数
     * 
     * @param bjParamConfigCost 成本参数
     * @return 结果
     */
    @Override
    public int updateBjParamConfigCost(BjParamConfigCost bjParamConfigCost)
    {
        return bjParamConfigCostMapper.updateBjParamConfigCost(bjParamConfigCost);
    }

    /**
     * 批量删除成本参数
     * 
     * @param ids 需要删除的成本参数主键
     * @return 结果
     */
    @Override
    public int deleteBjParamConfigCostByIds(Long[] ids)
    {
        return bjParamConfigCostMapper.deleteBjParamConfigCostByIds(ids);
    }

    /**
     * 删除成本参数信息
     * 
     * @param id 成本参数主键
     * @return 结果
     */
    @Override
    public int deleteBjParamConfigCostById(Long id)
    {
        return bjParamConfigCostMapper.deleteBjParamConfigCostById(id);
    }
}
