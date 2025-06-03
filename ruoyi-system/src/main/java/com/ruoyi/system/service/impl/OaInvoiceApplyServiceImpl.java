package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaInvoiceApplyMapper;
import com.ruoyi.system.domain.OaInvoiceApply;
import com.ruoyi.system.service.IOaInvoiceApplyService;

/**
 * 开票申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaInvoiceApplyServiceImpl implements IOaInvoiceApplyService 
{
    @Autowired
    private OaInvoiceApplyMapper oaInvoiceApplyMapper;

    /**
     * 查询开票申请
     * 
     * @param id 开票申请主键
     * @return 开票申请
     */
    @Override
    public OaInvoiceApply selectOaInvoiceApplyById(Long id)
    {
        return oaInvoiceApplyMapper.selectOaInvoiceApplyById(id);
    }

    /**
     * 查询开票申请列表
     * 
     * @param oaInvoiceApply 开票申请
     * @return 开票申请
     */
    @Override
    public List<OaInvoiceApply> selectOaInvoiceApplyList(OaInvoiceApply oaInvoiceApply)
    {
        return oaInvoiceApplyMapper.selectOaInvoiceApplyList(oaInvoiceApply);
    }

    /**
     * 新增开票申请
     * 
     * @param oaInvoiceApply 开票申请
     * @return 结果
     */
    @Override
    public int insertOaInvoiceApply(OaInvoiceApply oaInvoiceApply)
    {
        return oaInvoiceApplyMapper.insertOaInvoiceApply(oaInvoiceApply);
    }

    /**
     * 修改开票申请
     * 
     * @param oaInvoiceApply 开票申请
     * @return 结果
     */
    @Override
    public int updateOaInvoiceApply(OaInvoiceApply oaInvoiceApply)
    {
        return oaInvoiceApplyMapper.updateOaInvoiceApply(oaInvoiceApply);
    }

    /**
     * 批量删除开票申请
     * 
     * @param ids 需要删除的开票申请主键
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceApplyByIds(Long[] ids)
    {
        return oaInvoiceApplyMapper.deleteOaInvoiceApplyByIds(ids);
    }

    /**
     * 删除开票申请信息
     * 
     * @param id 开票申请主键
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceApplyById(Long id)
    {
        return oaInvoiceApplyMapper.deleteOaInvoiceApplyById(id);
    }
}
