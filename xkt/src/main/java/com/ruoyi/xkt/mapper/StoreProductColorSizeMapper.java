package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSizeDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnResDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnResDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口商品颜色的尺码Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductColorSizeMapper extends BaseMapper<StoreProductColorSize> {

    /**
     * 将商品颜色价格尺码置为无效
     *
     * @param storeProdId 档口商品ID
     */
    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 根据商品ID查询商品尺码列表
     *
     * @param storeProdId 档口商品ID
     * @return List<StoreProdColorSizeDTO>
     */
    List<StoreProdSizeDTO> selectListByStoreProdId(Long storeProdId);

    /**
     * 查询步橘网的条码信息
     *
     * @param snPrefix   条码前缀
     * @param storeId    档口ID
     * @param storeCusId 档口客户ID
     * @return StoreSaleBarcodeResDTO
     */
    StoreSaleSnResDTO selectSn(@Param("snPrefix") String snPrefix, @Param("storeId") String storeId, @Param("storeCusId") Long storeCusId);

    /**
     * 查询其它系统条码信息
     *
     * @param snPrefix   条码前缀
     * @param storeId    档口ID
     * @param storeCusId 档口客户ID
     * @return StoreSaleBarcodeResDTO
     */
    StoreSaleSnResDTO selectOtherSn(@Param("snPrefix") String snPrefix, @Param("storeId") String storeId, @Param("storeCusId") Long storeCusId);

    /**
     * 查询普通商品的条码信息
     *
     * @param snList 条码列表
     * @return List<StoreProdSnsResDTO.SPSDetailDTO>
     */
    List<StoreProdSnResDTO.SPSDetailDTO> selectSnList(@Param("snList") List<String> snList, @Param("storeId") String storeId);
}
