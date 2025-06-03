package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaProjectPayment;

/**
 * 项目款项Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaProjectPaymentService 
{
    /**
     * 查询项目款项
     * 
     * @param id 项目款项主键
     * @return 项目款项
     */
    public OaProjectPayment selectOaProjectPaymentById(Long id);

    /**
     * 查询项目款项列表
     * 
     * @param oaProjectPayment 项目款项
     * @return 项目款项集合
     */
    public List<OaProjectPayment> selectOaProjectPaymentList(OaProjectPayment oaProjectPayment);

    /**
     * 新增项目款项
     * 
     * @param oaProjectPayment 项目款项
     * @return 结果
     */
    public int insertOaProjectPayment(OaProjectPayment oaProjectPayment);

    /**
     * 修改项目款项
     * 
     * @param oaProjectPayment 项目款项
     * @return 结果
     */
    public int updateOaProjectPayment(OaProjectPayment oaProjectPayment);

    /**
     * 批量删除项目款项
     * 
     * @param ids 需要删除的项目款项主键集合
     * @return 结果
     */
    public int deleteOaProjectPaymentByIds(Long[] ids);

    /**
     * 删除项目款项信息
     * 
     * @param id 项目款项主键
     * @return 结果
     */
    public int deleteOaProjectPaymentById(Long id);
}
