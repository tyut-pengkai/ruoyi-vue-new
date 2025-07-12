package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSizeDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnResDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreStockTakingSnTempDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreStorageSnDTO;
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
     * 获取步橘网入库的条码信息
     * @param storeId 档口ID
     * @param buJuPrefixSnList 步橘网条码列表
     * @return
     */
    List<StoreStorageSnDTO.SSSDetailDTO> selectStorageBuJuSnList(@Param("storeId") String storeId, @Param("buJuPrefixSnList") List<String> buJuPrefixSnList);

    /**
     * 获取其它系统入库的条码信息
     * @param storeId 档口id
     * @param otherPrefixSnList 步橘网条码列表
     * @return  List<StoreStorageSnDTO.SSSDetailDTO>
     */
    List<StoreStorageSnDTO.SSSDetailDTO> selectStorageOtherSnList(@Param("storeId") String storeId, @Param("otherPrefixSnList") List<String> otherPrefixSnList);


    /**
     * 获取步橘网档口商品盘点的条码信息
     *
     * @param storeId           档口ID
     * @param buJuPrefixSnList  步橘网条码列表
     * @return List<StoreStockTakingSnTempDTO.SSTSTDetailDTO>
     */
    List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> selectStockBuJuSnList(@Param("storeId") String storeId, @Param("buJuPrefixSnList") List<String> buJuPrefixSnList);

    /**
     * 获取其它系统档口商品盘点的条码信息
     * @param storeId 档口ID
     * @param otherPrefixSnList 其它系统条码列表
     * @return List<StoreStockTakingSnTempDTO.SSTSTDetailDTO>
     */
    List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> selectStockOtherSnList(@Param("storeId") String storeId, @Param("otherPrefixSnList") List<String> otherPrefixSnList);

}

