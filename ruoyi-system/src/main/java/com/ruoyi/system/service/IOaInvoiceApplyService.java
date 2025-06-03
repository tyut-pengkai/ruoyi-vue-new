package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaInvoiceApply;

/**
 * 开票申请Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaInvoiceApplyService 
{
    /**
     * 查询开票申请
     * 
     * @param id 开票申请主键
     * @return 开票申请
     */
    public OaInvoiceApply selectOaInvoiceApplyById(Long id);

    /**
     * 查询开票申请列表
     * 
     * @param oaInvoiceApply 开票申请
     * @return 开票申请集合
     */
    public List<OaInvoiceApply> selectOaInvoiceApplyList(OaInvoiceApply oaInvoiceApply);

    /**
     * 新增开票申请
     * 
     * @param oaInvoiceApply 开票申请
     * @return 结果
     */
    public int insertOaInvoiceApply(OaInvoiceApply oaInvoiceApply);

    /**
     * 修改开票申请
     * 
     * @param oaInvoiceApply 开票申请
     * @return 结果
     */
    public int updateOaInvoiceApply(OaInvoiceApply oaInvoiceApply);

    /**
     * 批量删除开票申请
     * 
     * @param ids 需要删除的开票申请主键集合
     * @return 结果
     */
    public int deleteOaInvoiceApplyByIds(Long[] ids);

    /**
     * 删除开票申请信息
     * 
     * @param id 开票申请主键
     * @return 结果
     */
    public int deleteOaInvoiceApplyById(Long id);
}
