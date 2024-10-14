package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCostLaborMapper;
import com.ruoyi.system.domain.TCostLabor;
import com.ruoyi.system.service.ITCostLaborService;

/**
 * 人工报价成本Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-12
 */
@Service
public class TCostLaborServiceImpl implements ITCostLaborService 
{
    @Autowired
    private TCostLaborMapper tCostLaborMapper;

    /**
     * 查询人工报价成本
     * 
     * @param id 人工报价成本主键
     * @return 人工报价成本
     */
    @Override
    public TCostLabor selectTCostLaborById(Long id)
    {
        return tCostLaborMapper.selectTCostLaborById(id);
    }

    /**
     * 查询人工报价成本列表
     * 
     * @param tCostLabor 人工报价成本
     * @return 人工报价成本
     */
    @Override
    public List<TCostLabor> selectTCostLaborList(TCostLabor tCostLabor)
    {
        return tCostLaborMapper.selectTCostLaborList(tCostLabor);
    }

    /**
     * 新增人工报价成本
     * 
     * @param tCostLabor 人工报价成本
     * @return 结果
     */
    @Override
    public int insertTCostLabor(TCostLabor tCostLabor)
    {
        tCostLabor.setCreateTime(DateUtils.getNowDate());
        return tCostLaborMapper.insertTCostLabor(tCostLabor);
    }

    /**
     * 修改人工报价成本
     * 
     * @param tCostLabor 人工报价成本
     * @return 结果
     */
    @Override
    public int updateTCostLabor(TCostLabor tCostLabor)
    {
        return tCostLaborMapper.updateTCostLabor(tCostLabor);
    }

    /**
     * 批量删除人工报价成本
     * 
     * @param ids 需要删除的人工报价成本主键
     * @return 结果
     */
    @Override
    public int deleteTCostLaborByIds(Long[] ids)
    {
        return tCostLaborMapper.deleteTCostLaborByIds(ids);
    }

    /**
     * 删除人工报价成本信息
     * 
     * @param id 人工报价成本主键
     * @return 结果
     */
    @Override
    public int deleteTCostLaborById(Long id)
    {
        return tCostLaborMapper.deleteTCostLaborById(id);
    }
}
