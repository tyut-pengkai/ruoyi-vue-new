package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 档口当前商品颜色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductColorMapper extends BaseMapper<StoreProductColor> {

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
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdColorResDTO>
     */
    List<StoreProdColorResDTO> fuzzyQueryColorList(@Param("storeId") Long storeId, @Param("prodArtNum") String prodArtNum);

}
