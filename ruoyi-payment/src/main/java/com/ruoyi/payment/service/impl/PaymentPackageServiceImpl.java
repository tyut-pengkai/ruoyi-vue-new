package com.ruoyi.payment.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.payment.mapper.PaymentPackageMapper;
import com.ruoyi.payment.domain.PaymentPackage;
import com.ruoyi.payment.service.IPaymentPackageService;

/**
 * 商品套餐Service业务层处理
 * 
 * @author auto
 * @date 2025-06-24
 */
@Service
public class PaymentPackageServiceImpl implements IPaymentPackageService 
{
    @Autowired
    private PaymentPackageMapper paymentPackageMapper;

    /**
     * 查询商品套餐
     * 
     * @param packageId 商品套餐主键
     * @return 商品套餐
     */
    @Override
    public PaymentPackage selectPaymentPackageByPackageId(Long packageId)
    {
        return paymentPackageMapper.selectPaymentPackageByPackageId(packageId);
    }

    /**
     * 查询商品套餐列表
     * 
     * @param paymentPackage 商品套餐
     * @return 商品套餐
     */
    @Override
    public List<PaymentPackage> selectPaymentPackageList(PaymentPackage paymentPackage)
    {
        return paymentPackageMapper.selectPaymentPackageList(paymentPackage);
    }

    /**
     * 新增商品套餐
     * 
     * @param paymentPackage 商品套餐
     * @return 结果
     */
    @Override
    public int insertPaymentPackage(PaymentPackage paymentPackage)
    {
        paymentPackage.setCreateTime(DateUtils.getNowDate());
        return paymentPackageMapper.insertPaymentPackage(paymentPackage);
    }

    /**
     * 修改商品套餐
     * 
     * @param paymentPackage 商品套餐
     * @return 结果
     */
    @Override
    public int updatePaymentPackage(PaymentPackage paymentPackage)
    {
        paymentPackage.setUpdateTime(DateUtils.getNowDate());
        return paymentPackageMapper.updatePaymentPackage(paymentPackage);
    }

    /**
     * 批量删除商品套餐
     * 
     * @param packageIds 需要删除的商品套餐主键
     * @return 结果
     */
    @Override
    public int deletePaymentPackageByPackageIds(Long[] packageIds)
    {
        return paymentPackageMapper.deletePaymentPackageByPackageIds(packageIds);
    }

    /**
     * 删除商品套餐信息
     * 
     * @param packageId 商品套餐主键
     * @return 结果
     */
    @Override
    public int deletePaymentPackageByPackageId(Long packageId)
    {
        return paymentPackageMapper.deletePaymentPackageByPackageId(packageId);
    }

    /**
     * 查询可用的商品套餐列表
     *
     * @return 商品套餐集合
     */
    @Override
    public List<PaymentPackage> selectAvailablePackageList()
    {
        PaymentPackage paymentPackage = new PaymentPackage();
        paymentPackage.setStatus("0"); // 0=启用
        return paymentPackageMapper.selectPaymentPackageList(paymentPackage);
    }
}
