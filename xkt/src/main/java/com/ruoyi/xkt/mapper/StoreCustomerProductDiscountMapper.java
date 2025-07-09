package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.dto.storeCusProdDiscount.StoreCusProdDiscPageDTO;
import com.ruoyi.xkt.dto.storeCusProdDiscount.StoreCusProdDiscPageResDTO;

import java.util.List;

/**
 * 档口客户优惠Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreCustomerProductDiscountMapper extends BaseMapper<StoreCustomerProductDiscount> {

    /**
     * 档口客户优惠分页
     *
     * @param pageDTO 分页入参
     * @return List<StoreCusProdDiscPageResDTO>
     */
    List<StoreCusProdDiscPageResDTO> selectDiscPage(StoreCusProdDiscPageDTO pageDTO);

}
