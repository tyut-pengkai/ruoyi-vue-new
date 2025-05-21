package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;

import java.util.List;

/**
 * 档口销售出库Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleMapper extends BaseMapper<StoreSale> {

    List<StoreSalePageResDTO> selectPage(StoreSalePageDTO pageDTO);

}
