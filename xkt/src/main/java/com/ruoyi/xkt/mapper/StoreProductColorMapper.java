package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProductColorFuzzyPageDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPricePageDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdStatusCountResDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口当前商品颜色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductColorMapper extends BaseMapper<StoreProductColor> {

    /**
     * 将商品颜色置为无效
     *
     * @param storeProdId 档口商品ID
     */
    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 获取档口商品列表
     *
     * @param storeProdId 档口商品ID
     * @return List<StoreProdColorDTO>
     */
    List<StoreProdColorDTO> selectListByStoreProdId(Long storeProdId);

    /**
     * 获取档口商品颜色列表
     *
     * @param pageDTO 入参
     * @return
     */
    List<StoreProdPageResDTO> selectStoreProdColorPage(StoreProdPageDTO pageDTO);

    /**
     * 输入商品货号模糊查询颜色分类
     *
     * @param pageDTO 查询入参
     * @return List<StoreProdColorResDTO>
     */
    List<StoreProdColorResDTO> fuzzyQueryColorList(StoreProductColorFuzzyPageDTO pageDTO);

    /**
     * 获取商品颜色价格列表
     *
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @return List<StoreProdColorPriceResDTO>
     */
    List<StoreProdColorPriceResDTO> selectListByStoreProdIdAndStoreId(@Param("storeProdId") Long storeProdId, @Param("storeId") Long storeId);

    /**
     * 新增客户优惠获取所有的颜色及价格
     *
     * @param pageDTO 入参
     * @return List<StoreProdColorPriceResDTO>
     */
    List<StoreProdColorPriceResDTO> selectColorPricePage(StoreProdColorPricePageDTO pageDTO);

    /**
     * 获取档口各个状态的数量
     *
     * @param storeId 档口ID
     * @return StoreProdStatusCountResDTO
     */
    StoreProdStatusCountResDTO getStatusNum(@Param("storeId") Long storeId);
}
