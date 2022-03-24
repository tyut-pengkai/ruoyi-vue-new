package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysPayment;

import java.util.List;

/**
 * 支付配置Service接口
 *
 * @author zwgu
 * @date 2022-03-24
 */
public interface ISysPaymentService {
    /**
     * 查询支付配置
     *
     * @param payId 支付配置主键
     * @return 支付配置
     */
    public SysPayment selectSysPaymentByPayId(Long payId);

    /**
     * 查询支付配置
     *
     * @param payCode 支付配置主键
     * @return 支付配置
     */
    public SysPayment selectSysPaymentByPayCode(String payCode);

    /**
     * 查询支付配置列表
     *
     * @param sysPayment 支付配置
     * @return 支付配置集合
     */
    public List<SysPayment> selectSysPaymentList(SysPayment sysPayment);

    /**
     * 新增支付配置
     *
     * @param sysPayment 支付配置
     * @return 结果
     */
    public int insertSysPayment(SysPayment sysPayment);

    /**
     * 修改支付配置
     *
     * @param sysPayment 支付配置
     * @return 结果
     */
    public int updateSysPayment(SysPayment sysPayment);

    /**
     * 批量删除支付配置
     *
     * @param payIds 需要删除的支付配置主键集合
     * @return 结果
     */
    public int deleteSysPaymentByPayIds(Long[] payIds);

    /**
     * 删除支付配置信息
     *
     * @param payId 支付配置主键
     * @return 结果
     */
    public int deleteSysPaymentByPayId(Long payId);
}
