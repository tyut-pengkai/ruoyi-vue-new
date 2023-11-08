package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.mapper.SysPaymentMapper;
import com.ruoyi.system.service.ISysPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付配置Service业务层处理
 *
 * @author zwgu
 * @date 2022-03-24
 */
@Service
public class SysPaymentServiceImpl implements ISysPaymentService {
    @Autowired
    private SysPaymentMapper sysPaymentMapper;

    /**
     * 查询支付配置
     *
     * @param payId 支付配置主键
     * @return 支付配置
     */
    @Override
    public SysPayment selectSysPaymentByPayId(Long payId) {
        return sysPaymentMapper.selectSysPaymentByPayId(payId);
    }

    /**
     * 查询支付配置
     *
     * @param payCode 支付配置主键
     * @return 支付配置
     */
    @Override
    public SysPayment selectSysPaymentByPayCode(String payCode) {
        return sysPaymentMapper.selectSysPaymentByPayCode(payCode);
    }

    /**
     * 查询支付配置列表
     *
     * @param sysPayment 支付配置
     * @return 支付配置
     */
    @Override
    public List<SysPayment> selectSysPaymentList(SysPayment sysPayment) {
        return sysPaymentMapper.selectSysPaymentList(sysPayment);
    }

    /**
     * 新增支付配置
     *
     * @param sysPayment 支付配置
     * @return 结果
     */
    @Override
    public int insertSysPayment(SysPayment sysPayment) {
        sysPayment.setCreateTime(DateUtils.getNowDate());
        sysPayment.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysPaymentMapper.insertSysPayment(sysPayment);
    }

    /**
     * 修改支付配置
     *
     * @param sysPayment 支付配置
     * @return 结果
     */
    @Override
    public int updateSysPayment(SysPayment sysPayment) {
        sysPayment.setUpdateTime(DateUtils.getNowDate());
        sysPayment.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysPaymentMapper.updateSysPayment(sysPayment);
    }

    /**
     * 批量删除支付配置
     *
     * @param payIds 需要删除的支付配置主键
     * @return 结果
     */
    @Override
    public int deleteSysPaymentByPayIds(Long[] payIds) {
        return sysPaymentMapper.deleteSysPaymentByPayIds(payIds);
    }

    /**
     * 删除支付配置信息
     *
     * @param payId 支付配置主键
     * @return 结果
     */
    @Override
    public int deleteSysPaymentByPayId(Long payId) {
        return sysPaymentMapper.deleteSysPaymentByPayId(payId);
    }
}
