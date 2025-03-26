package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;

import java.util.List;

/**
 * 档口客户优惠Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCustomerProductDiscountService {
    /**
     * 查询档口客户优惠
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 档口客户优惠
     */
    public StoreCustomerProductDiscount selectStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId);

    /**
     * 查询档口客户优惠列表
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 档口客户优惠集合
     */
    public List<StoreCustomerProductDiscount> selectStoreCustomerProductDiscountList(StoreCustomerProductDiscount storeCustomerProductDiscount);

    /**
     * 新增档口客户优惠
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 结果
     */
    public int insertStoreCustomerProductDiscount(StoreCustomerProductDiscount storeCustomerProductDiscount);

    /**
     * 修改档口客户优惠
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 结果
     */
    public int updateStoreCustomerProductDiscount(StoreCustomerProductDiscount storeCustomerProductDiscount);

    /**
     * 批量删除档口客户优惠
     *
     * @param storeCusProdDiscIds 需要删除的档口客户优惠主键集合
     * @return 结果
     */
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(Long[] storeCusProdDiscIds);

    /**
     * 删除档口客户优惠信息
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 结果
     */
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId);
}
