package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaProjectPaymentMapper;
import com.ruoyi.system.domain.OaProjectPayment;
import com.ruoyi.system.service.IOaProjectPaymentService;

/**
 * 项目款项Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaProjectPaymentServiceImpl implements IOaProjectPaymentService 
{
    @Autowired
    private OaProjectPaymentMapper oaProjectPaymentMapper;

    /**
     * 查询项目款项
     * 
     * @param id 项目款项主键
     * @return 项目款项
     */
    @Override
    public OaProjectPayment selectOaProjectPaymentById(Long id)
    {
        return oaProjectPaymentMapper.selectOaProjectPaymentById(id);
    }

    /**
     * 查询项目款项列表
     * 
     * @param oaProjectPayment 项目款项
     * @return 项目款项
     */
    @Override
    public List<OaProjectPayment> selectOaProjectPaymentList(OaProjectPayment oaProjectPayment)
    {
        return oaProjectPaymentMapper.selectOaProjectPaymentList(oaProjectPayment);
    }

    /**
     * 新增项目款项
     * 
     * @param oaProjectPayment 项目款项
     * @return 结果
     */
    @Override
    public int insertOaProjectPayment(OaProjectPayment oaProjectPayment)
    {
        return oaProjectPaymentMapper.insertOaProjectPayment(oaProjectPayment);
    }

    /**
     * 修改项目款项
     * 
     * @param oaProjectPayment 项目款项
     * @return 结果
     */
    @Override
    public int updateOaProjectPayment(OaProjectPayment oaProjectPayment)
    {
        return oaProjectPaymentMapper.updateOaProjectPayment(oaProjectPayment);
    }

    /**
     * 批量删除项目款项
     * 
     * @param ids 需要删除的项目款项主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectPaymentByIds(Long[] ids)
    {
        return oaProjectPaymentMapper.deleteOaProjectPaymentByIds(ids);
    }

    /**
     * 删除项目款项信息
     * 
     * @param id 项目款项主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectPaymentById(Long id)
    {
        return oaProjectPaymentMapper.deleteOaProjectPaymentById(id);
    }
}
