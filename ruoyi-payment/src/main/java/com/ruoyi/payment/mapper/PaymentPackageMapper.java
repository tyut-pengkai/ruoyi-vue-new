package com.ruoyi.payment.mapper;

import java.util.List;
import com.ruoyi.payment.domain.PaymentPackage;

/**
 * 商品套餐Mapper接口
 * 
 * @author auto
 * @date 2025-06-24
 */
public interface PaymentPackageMapper 
{
    /**
     * 查询商品套餐
     * 
     * @param packageId 商品套餐主键
     * @return 商品套餐
     */
    public PaymentPackage selectPaymentPackageByPackageId(Long packageId);

    /**
     * 查询商品套餐列表
     * 
     * @param paymentPackage 商品套餐
     * @return 商品套餐集合
     */
    public List<PaymentPackage> selectPaymentPackageList(PaymentPackage paymentPackage);

    /**
     * 新增商品套餐
     * 
     * @param paymentPackage 商品套餐
     * @return 结果
     */
    public int insertPaymentPackage(PaymentPackage paymentPackage);

    /**
     * 修改商品套餐
     * 
     * @param paymentPackage 商品套餐
     * @return 结果
     */
    public int updatePaymentPackage(PaymentPackage paymentPackage);

    /**
     * 删除商品套餐
     * 
     * @param packageId 商品套餐主键
     * @return 结果
     */
    public int deletePaymentPackageByPackageId(Long packageId);

    /**
     * 批量删除商品套餐
     * 
     * @param packageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaymentPackageByPackageIds(Long[] packageIds);
}
