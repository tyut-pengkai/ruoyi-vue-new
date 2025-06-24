package com.ruoyi.payment.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.payment.domain.PaymentPackage;
import com.ruoyi.payment.mapper.PaymentPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.payment.mapper.PaymentOrderMapper;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.service.IPaymentOrderService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;

import java.util.Date;

/**
 * 支付订单Service业务层处理
 * 
 * @author auto
 * @date 2025-06-24
 */
@Service
public class PaymentOrderServiceImpl implements IPaymentOrderService 
{
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private PaymentPackageMapper paymentPackageMapper;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询支付订单
     * 
     * @param orderId 支付订单主键
     * @return 支付订单
     */
    @Override
    public PaymentOrder selectPaymentOrderByOrderId(Long orderId)
    {
        return paymentOrderMapper.selectPaymentOrderByOrderId(orderId);
    }

    /**
     * 查询支付订单列表
     * 
     * @param paymentOrder 支付订单
     * @return 支付订单
     */
    @Override
    public List<PaymentOrder> selectPaymentOrderList(PaymentOrder paymentOrder)
    {
        return paymentOrderMapper.selectPaymentOrderList(paymentOrder);
    }

    /**
     * 新增支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    @Override
    public int insertPaymentOrder(PaymentOrder paymentOrder)
    {
        paymentOrder.setCreateTime(DateUtils.getNowDate());
        return paymentOrderMapper.insertPaymentOrder(paymentOrder);
    }

    /**
     * 修改支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    @Override
    public int updatePaymentOrder(PaymentOrder paymentOrder)
    {
        paymentOrder.setUpdateTime(DateUtils.getNowDate());
        return paymentOrderMapper.updatePaymentOrder(paymentOrder);
    }

    /**
     * 批量删除支付订单
     * 
     * @param orderIds 需要删除的支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderByOrderIds(Long[] orderIds)
    {
        return paymentOrderMapper.deletePaymentOrderByOrderIds(orderIds);
    }

    /**
     * 删除支付订单信息
     * 
     * @param orderId 支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderByOrderId(Long orderId)
    {
        return paymentOrderMapper.deletePaymentOrderByOrderId(orderId);
    }

    /**
     * 创建支付订单
     *
     * @param userId 用户ID
     * @param packageId 套餐ID
     * @return 支付订单
     */
    @Override
    @Transactional
    public PaymentOrder createPaymentOrder(Long userId, Long packageId) {
        // 1. 获取套餐信息
        PaymentPackage paymentPackage = paymentPackageMapper.selectPaymentPackageByPackageId(packageId);
        if (paymentPackage == null) {
            throw new RuntimeException("套餐不存在");
        }

        // 2. 创建订单对象并填充数据
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOrderNo(IdUtils.fastSimpleUUID());
        paymentOrder.setUserId(userId);
        paymentOrder.setPackageId(packageId);
        paymentOrder.setPackageName(paymentPackage.getName());
        if (paymentPackage.getHours() != null) {
            paymentOrder.setPackageHours(paymentPackage.getHours().intValue());
        }
        paymentOrder.setAmount(paymentPackage.getPrice());
        paymentOrder.setCurrency(paymentPackage.getCurrency());
        paymentOrder.setStatus("0"); // 0=待支付
        paymentOrder.setCreateTime(DateUtils.getNowDate());
        //  TODO: 设置创建人
        //  paymentOrder.setCreateBy(SecurityUtils.getUsername());

        // 3. 插入订单到数据库
        paymentOrderMapper.insertPaymentOrder(paymentOrder);

        return paymentOrder;
    }

    /**
     * 处理支付成功逻辑
     *
     * @param orderNo 订单号
     * @return 结果
     */
    @Override
    @Transactional
    public boolean processPaymentSuccess(String orderNo)
    {
       
       
        

        return true;
    }
}
