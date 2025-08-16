package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorPrice;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPricePageDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceSimpleDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockAndDiscountDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockAndDiscountResDTO;
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


    /**
     * 商品列表编辑商品时 获取颜色列表
     * @param storeProdId 档口商品ID
     * @return List<StoreProdColorPriceSimpleDTO>
     */
    List<StoreProdColorPriceSimpleDTO> selectListByStoreProdId(Long storeProdId);

    /**
     * 根据档口商品ID及档口ID获取所有颜色列表及定价
     *
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @return List<StoreProdColorPriceResDTO>
     */
    List<StoreProdColorPriceResDTO> selectListByStoreProdIdAndStoreId(@Param("storeProdId") Long storeProdId, @Param("storeId") Long storeId);

    /**
     * 获取档口商品的最低定价
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<StoreProdMinPriceDTO>
     */
    List<StoreProdMinPriceDTO> selectStoreProdMinPriceList(@Param("storeProdIdList") List<String> storeProdIdList);

    /**
     * 获取档口颜色价格分页
     * @param pageDTO 入参
     * @return  List<StoreProdColorPriceResDTO>
     */
    List<StoreProdColorPriceResDTO> selectPricePage(StoreProdColorPricePageDTO pageDTO);

    /**
     * 销售出库，输入货号，查询颜色价格等信息
     * @param dto 入参
     * @return StoreProdStockAndDiscountResDTO
     */
    StoreProdStockAndDiscountResDTO selectStockAndCusDiscount(StoreProdStockAndDiscountDTO dto);
}
