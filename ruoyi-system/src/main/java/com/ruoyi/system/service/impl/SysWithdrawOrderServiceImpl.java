package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysWithdrawOrder;
import com.ruoyi.system.domain.vo.WithdrawCashVo;
import com.ruoyi.system.mapper.SysWithdrawOrderMapper;
import com.ruoyi.system.service.ISysWithdrawOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提现记录Service业务层处理
 *
 * @author zwgu
 * @date 2024-06-03
 */
@Service
public class SysWithdrawOrderServiceImpl implements ISysWithdrawOrderService {
    @Autowired
    private SysWithdrawOrderMapper sysWithdrawOrderMapper;

    /**
     * 查询提现记录
     *
     * @param id 提现记录主键
     * @return 提现记录
     */
    @Override
    public SysWithdrawOrder selectSysWithdrawOrderById(Long id) {
        return sysWithdrawOrderMapper.selectSysWithdrawOrderById(id);
    }

    /**
     * 查询提现记录列表
     *
     * @param sysWithdrawOrder 提现记录
     * @return 提现记录
     */
    @Override
    public List<SysWithdrawOrder> selectSysWithdrawOrderList(SysWithdrawOrder sysWithdrawOrder) {
        return sysWithdrawOrderMapper.selectSysWithdrawOrderList(sysWithdrawOrder);
    }

    /**
     * 新增提现记录
     *
     * @param sysWithdrawOrder 提现记录
     * @return 结果
     */
    @Override
    public int insertSysWithdrawOrder(SysWithdrawOrder sysWithdrawOrder) {
        sysWithdrawOrder.setCreateTime(DateUtils.getNowDate());
        sysWithdrawOrder.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysWithdrawOrderMapper.insertSysWithdrawOrder(sysWithdrawOrder);
    }

    /**
     * 修改提现记录
     *
     * @param sysWithdrawOrder 提现记录
     * @return 结果
     */
    @Override
    public int updateSysWithdrawOrder(SysWithdrawOrder sysWithdrawOrder) {
        sysWithdrawOrder.setUpdateTime(DateUtils.getNowDate());
        sysWithdrawOrder.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysWithdrawOrderMapper.updateSysWithdrawOrder(sysWithdrawOrder);
    }

    /**
     * 批量删除提现记录
     *
     * @param ids 需要删除的提现记录主键
     * @return 结果
     */
    @Override
    public int deleteSysWithdrawOrderByIds(Long[] ids) {
        return sysWithdrawOrderMapper.deleteSysWithdrawOrderByIds(ids);
    }

    /**
     * 删除提现记录信息
     *
     * @param id 提现记录主键
     * @return 结果
     */
    @Override
    public int deleteSysWithdrawOrderById(Long id) {
        return sysWithdrawOrderMapper.deleteSysWithdrawOrderById(id);
    }

    @Override
    public AjaxResult createWithdrawOrder(WithdrawCashVo vo) {
        return null;
    }
}
