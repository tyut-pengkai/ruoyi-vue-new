package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCostQuotationParamMapper;
import com.ruoyi.system.domain.TCostQuotationParam;
import com.ruoyi.system.service.ITCostQuotationParamService;

/**
 * 成本报价参数Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-11
 */
@Service
public class TCostQuotationParamServiceImpl implements ITCostQuotationParamService 
{
    @Autowired
    private TCostQuotationParamMapper tCostQuotationParamMapper;

    /**
     * 查询成本报价参数
     * 
     * @param id 成本报价参数主键
     * @return 成本报价参数
     */
    @Override
    public TCostQuotationParam selectTCostQuotationParamById(Long id)
    {
        return tCostQuotationParamMapper.selectTCostQuotationParamById(id);
    }

    /**
     * 查询成本报价参数列表
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 成本报价参数
     */
    @Override
    public List<TCostQuotationParam> selectTCostQuotationParamList(TCostQuotationParam tCostQuotationParam)
    {
        return tCostQuotationParamMapper.selectTCostQuotationParamList(tCostQuotationParam);
    }

    /**
     * 新增成本报价参数
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 结果
     */
    @Override
    public int insertTCostQuotationParam(TCostQuotationParam tCostQuotationParam)
    {
        return tCostQuotationParamMapper.insertTCostQuotationParam(tCostQuotationParam);
    }

    /**
     * 修改成本报价参数
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 结果
     */
    @Override
    public int updateTCostQuotationParam(TCostQuotationParam tCostQuotationParam)
    {
        return tCostQuotationParamMapper.updateTCostQuotationParam(tCostQuotationParam);
    }

    /**
     * 批量删除成本报价参数
     * 
     * @param ids 需要删除的成本报价参数主键
     * @return 结果
     */
    @Override
    public int deleteTCostQuotationParamByIds(Long[] ids)
    {
        return tCostQuotationParamMapper.deleteTCostQuotationParamByIds(ids);
    }

    /**
     * 删除成本报价参数信息
     * 
     * @param id 成本报价参数主键
     * @return 结果
     */
    @Override
    public int deleteTCostQuotationParamById(Long id)
    {
        return tCostQuotationParamMapper.deleteTCostQuotationParamById(id);
    }
}
