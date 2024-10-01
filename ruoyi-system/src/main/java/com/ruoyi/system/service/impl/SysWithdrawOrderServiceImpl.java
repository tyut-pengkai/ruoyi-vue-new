package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.BalanceChangeType;
import com.ruoyi.common.enums.WithdrawStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.SysWithdrawMethod;
import com.ruoyi.system.domain.SysWithdrawOrder;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.domain.vo.UserBalanceWithdrawVo;
import com.ruoyi.system.mapper.SysWithdrawOrderMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysWithdrawOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private ISysUserService sysUserService;

    private static final SnowflakeIdWorker sf = new SnowflakeIdWorker();

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
    @Transactional(rollbackFor = Exception.class)
    public int updateSysWithdrawOrder(SysWithdrawOrder sysWithdrawOrder) {
        sysWithdrawOrder.setUpdateTime(DateUtils.getNowDate());
        sysWithdrawOrder.setUpdateBy(SecurityUtils.getUsernameNoException());
        SysWithdrawOrder o = sysWithdrawOrderMapper.selectSysWithdrawOrderById(sysWithdrawOrder.getId());
        // 仅允许变更提现状态
        SysWithdrawOrder order = new SysWithdrawOrder();
        order.setId(sysWithdrawOrder.getId());
        order.setWithdrawStatus(sysWithdrawOrder.getWithdrawStatus());
        String description = "提现到收款账号[" + o.getReceiveMethod() + "|" + o.getReceiveAccount() + "]" + "："
                + "申请：" + o.getApplyFee() + "，手续费：" + o.getHandlingFee() + "，实际：" + o.getActualFee();
        if(order.getWithdrawStatus() == WithdrawStatus.PAY_SUCCESS) {
            sysWithdrawOrder.setTradeTime(DateUtils.getNowDate());
            order.setTradeNo(sysWithdrawOrder.getTradeNo());
            // 扣除用户冻结余额
            BalanceChangeVo changeBalance = new BalanceChangeVo();
            changeBalance.setUserId(o.getUserId());
            changeBalance.setUpdateBy(SecurityUtils.getUsernameNoException());
            changeBalance.setType(BalanceChangeType.WITHDRAW_CASH_SUCCESS);
            changeBalance.setDescription(description);
            changeBalance.setFreezePayBalance(o.getActualFee().negate());
            changeBalance.setWithdrawCashId(o.getId());
            changeBalance.setSourceUserId(o.getUserId());
            sysUserService.updateUserBalance(changeBalance);
            // 扣除用户手续费
            BalanceChangeVo changeBalance2 = new BalanceChangeVo();
            changeBalance2.setUserId(o.getUserId());
            changeBalance2.setUpdateBy(SecurityUtils.getUsernameNoException());
            changeBalance2.setType(BalanceChangeType.WITHDRAW_CASH_SUCCESS);
            changeBalance2.setDescription(description);
            changeBalance2.setAvailablePayBalance(o.getHandlingFee().negate());
            changeBalance2.setWithdrawCashId(o.getId());
            changeBalance2.setSourceUserId(o.getUserId());
            sysUserService.updateUserBalance(changeBalance2);
        } else if(order.getWithdrawStatus() == WithdrawStatus.PAY_FAILED) {
            order.setErrorCode(sysWithdrawOrder.getErrorCode());
            order.setErrorMessage(sysWithdrawOrder.getErrorMessage());
            // 恢复用户余额
            BalanceChangeVo changeBalance = new BalanceChangeVo();
            changeBalance.setUserId(o.getUserId());
            changeBalance.setUpdateBy(SecurityUtils.getUsernameNoException());
            changeBalance.setType(BalanceChangeType.WITHDRAW_CASH_FAILED);
            changeBalance.setDescription(description);
            changeBalance.setAvailablePayBalance(o.getActualFee());
            changeBalance.setFreezePayBalance(o.getActualFee().negate());
            changeBalance.setWithdrawCashId(o.getId());
            changeBalance.setSourceUserId(o.getUserId());
            sysUserService.updateUserBalance(changeBalance);
        }
        order.setRemark(sysWithdrawOrder.getRemark());
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
    public Long createWithdrawOrder(UserBalanceWithdrawVo vo, SysWithdrawMethod withdrawMethod, BigDecimal expectActualFee, BigDecimal handlingFee) {
        // 2.订单生成
        String orderNo = genOrderNo();
        SysWithdrawOrder swo = new SysWithdrawOrder();
        swo.setOrderNo(orderNo);
        // 3.计算金额
        swo.setApplyFee(vo.getApplyFee());
        swo.setHandlingFee(handlingFee);
        swo.setActualFee(expectActualFee);
        // 4.获取收款方式
        swo.setWithdrawMethodId(vo.getReceiveAccountId());
        swo.setReceiveMethod(withdrawMethod.getReceiveMethod());
        swo.setReceiveAccount(withdrawMethod.getReceiveAccount());
        swo.setRealName(withdrawMethod.getRealName());

        swo.setErrorCode(null);
        swo.setErrorMessage(null);
        swo.setManualTransfer(UserConstants.YES);
        swo.setTradeNo(null);
        swo.setTradeTime(null);
        swo.setUserId(SecurityUtils.getUserId());
        swo.setWithdrawStatus(WithdrawStatus.WAIT_PAY);
        swo.setRemark(vo.getRemark());
        swo.setCreateBy(SecurityUtils.getUsernameNoException());
        sysWithdrawOrderMapper.insertSysWithdrawOrder(swo);
        return swo.getId();
    }

    private String genOrderNo() {
        return String.valueOf(sf.nextId());
    }

    /**
     * 删除提现记录信息
     *
     * @param id 提现记录主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelSysWithdrawOrderByIds(Long[] ids) {
        for (Long id : ids) {
            SysWithdrawOrder o = sysWithdrawOrderMapper.selectSysWithdrawOrderById(id);
            if(o != null) {
                if(!Objects.equals(o.getUserId(), SecurityUtils.getUserId())) {
                    throw new ServiceException("您没有权限取消该提现订单");
                }
                // 仅允许变更提现状态
                SysWithdrawOrder sysWithdrawOrder = new SysWithdrawOrder();
                sysWithdrawOrder.setId(o.getId());
                sysWithdrawOrder.setWithdrawStatus(WithdrawStatus.USER_CANCELED);
                String description = "用户取消提现，原提现信息：提现到收款账号[" + o.getReceiveMethod() + "|" + o.getReceiveAccount() + "]" + "："
                        + "申请：" + o.getApplyFee() + "，手续费：" + o.getHandlingFee() + "，实际：" + o.getActualFee();
                if(o.getWithdrawStatus() == WithdrawStatus.WAIT_PAY) {
                    // 恢复用户余额
                    BalanceChangeVo changeBalance = new BalanceChangeVo();
                    changeBalance.setUserId(o.getUserId());
                    changeBalance.setUpdateBy(SecurityUtils.getUsernameNoException());
                    changeBalance.setType(BalanceChangeType.WITHDRAW_CASH_CANCELED);
                    changeBalance.setDescription(description);
                    changeBalance.setAvailablePayBalance(o.getActualFee());
                    changeBalance.setFreezePayBalance(o.getActualFee().negate());
                    changeBalance.setWithdrawCashId(o.getId());
                    changeBalance.setSourceUserId(o.getUserId());
                    sysUserService.updateUserBalance(changeBalance);
                    sysWithdrawOrder.setUpdateTime(DateUtils.getNowDate());
                    sysWithdrawOrder.setUpdateBy(SecurityUtils.getUsernameNoException());
                    sysWithdrawOrderMapper.updateSysWithdrawOrder(sysWithdrawOrder);
                } else {
                    throw new ServiceException("撤销提现失败，订单状态已变更，请刷新页面后重试");
                }
            }
        }
        return 1;
    }
}
