package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.mapper.StoreCustomerProductDiscountMapper;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口客户优惠Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreCustomerProductDiscountServiceImpl implements IStoreCustomerProductDiscountService {
    @Autowired
    private StoreCustomerProductDiscountMapper storeCustomerProductDiscountMapper;

    /**
     * 查询档口客户优惠
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 档口客户优惠
     */
    @Override
    public StoreCustomerProductDiscount selectStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId) {
        return storeCustomerProductDiscountMapper.selectStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId);
    }

    /**
     * 查询档口客户优惠列表
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 档口客户优惠
     */
    @Override
    public List<StoreCustomerProductDiscount> selectStoreCustomerProductDiscountList(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        return storeCustomerProductDiscountMapper.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
    }

    /**
     * 新增档口客户优惠
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 结果
     */
    @Override
    public int insertStoreCustomerProductDiscount(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        storeCustomerProductDiscount.setCreateTime(DateUtils.getNowDate());
        return storeCustomerProductDiscountMapper.insertStoreCustomerProductDiscount(storeCustomerProductDiscount);
    }

    /**
     * 修改档口客户优惠
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 结果
     */
    @Override
    public int updateStoreCustomerProductDiscount(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        storeCustomerProductDiscount.setUpdateTime(DateUtils.getNowDate());
        return storeCustomerProductDiscountMapper.updateStoreCustomerProductDiscount(storeCustomerProductDiscount);
    }

    /**
     * 批量删除档口客户优惠
     *
     * @param storeCusProdDiscIds 需要删除的档口客户优惠主键
     * @return 结果
     */
    @Override
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(Long[] storeCusProdDiscIds) {
        return storeCustomerProductDiscountMapper.deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(storeCusProdDiscIds);
    }

    /**
     * 删除档口客户优惠信息
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 结果
     */
    @Override
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId) {
        return storeCustomerProductDiscountMapper.deleteStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId);
    }
}
