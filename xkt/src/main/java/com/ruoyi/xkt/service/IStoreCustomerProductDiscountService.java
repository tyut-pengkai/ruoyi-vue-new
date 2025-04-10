package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeCusProdDiscount.*;

/**
 * 档口客户优惠Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCustomerProductDiscountService {

    /**
     * 修改档口客户优惠
     *
     * @param cusProdDisDTO 档口客户优惠
     * @return 结果
     */
    Integer updateStoreCusProdDiscount(StoreCusProdDiscountDTO cusProdDisDTO);

    /**
     * 档口客户 批量减价、批量抹零减价
     *
     * @param batchDiscDTO 批量减价入参
     * @return Integer
     */
    Integer batchDiscount(StoreCusProdBatchDiscountDTO batchDiscDTO);

    /**
     * 查询客户销售管理列表
     *
     * @param pageDTO 分页入参
     * @return Page<StoreCusProdDiscPageResDTO>
     */
    Page<StoreCusProdDiscPageResDTO> selectPage(StoreCusProdDiscPageDTO pageDTO);

    /**
     * 客户销售管理，新增客户优惠时，判断是否已存在优惠
     * @param existDTO 优惠是否存在DTO
     * @return String
     */
    void discountExist(StoreCusProdDiscExistDTO existDTO);

}
