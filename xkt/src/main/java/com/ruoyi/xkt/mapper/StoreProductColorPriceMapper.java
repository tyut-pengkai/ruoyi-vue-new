package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorPrice;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceSimpleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口商品颜色定价Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductColorPriceMapper extends BaseMapper<StoreProductColorPrice> {

    void updateDelFlagByStoreProdId(Long storeProdId);

    List<StoreProdColorPriceSimpleDTO> selectListByStoreProdId(Long storeProdId);

    /**
     * 根据档口商品ID及档口ID获取所有颜色列表及定价
     *
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @return List<StoreProdColorPriceResDTO>
     */
    List<StoreProdColorPriceResDTO> selectListByStoreProdIdAndStoreId(@Param("storeProdId") Long storeProdId, @Param("storeId") Long storeId);

}
