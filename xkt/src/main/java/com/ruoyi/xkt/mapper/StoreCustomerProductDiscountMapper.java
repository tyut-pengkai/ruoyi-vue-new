package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;

import java.util.List;

/**
 * 档口客户优惠Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreCustomerProductDiscountMapper extends BaseMapper<StoreCustomerProductDiscount> {
    /**
     * 查询档口客户优惠
     *
     * @param id 档口客户优惠主键
     * @return 档口客户优惠
     */
    public StoreCustomerProductDiscount selectStoreCustomerProductDiscountByStoreCusProdDiscId(Long id);

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
     * 删除档口客户优惠
     *
     * @param id 档口客户优惠主键
     * @return 结果
     */
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscId(Long id);

    /**
     * 批量删除档口客户优惠
     *
     * @param storeCusProdDiscIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(Long[] storeCusProdDiscIds);
}
