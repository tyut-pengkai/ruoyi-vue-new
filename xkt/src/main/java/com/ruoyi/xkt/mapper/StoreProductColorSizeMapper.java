package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdColorSizeDTO;
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
     * 查询档口商品颜色的尺码
     *
     * @param id 档口商品颜色的尺码主键
     * @return 档口商品颜色的尺码
     */
    public StoreProductColorSize selectStoreProductColorSizeByStoreProdColorSizeId(Long id);

    /**
     * 查询档口商品颜色的尺码列表
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 档口商品颜色的尺码集合
     */
    public List<StoreProductColorSize> selectStoreProductColorSizeList(StoreProductColorSize storeProductColorSize);

    /**
     * 新增档口商品颜色的尺码
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 结果
     */
    public int insertStoreProductColorSize(StoreProductColorSize storeProductColorSize);

    /**
     * 修改档口商品颜色的尺码
     *
     * @param storeProductColorSize 档口商品颜色的尺码
     * @return 结果
     */
    public int updateStoreProductColorSize(StoreProductColorSize storeProductColorSize);

    /**
     * 删除档口商品颜色的尺码
     *
     * @param id 档口商品颜色的尺码主键
     * @return 结果
     */
    public int deleteStoreProductColorSizeByStoreProdColorSizeId(Long id);

    /**
     * 批量删除档口商品颜色的尺码
     *
     * @param storeProdColorSizeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductColorSizeByStoreProdColorSizeIds(Long[] storeProdColorSizeIds);

    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 根据商品ID查询商品尺码列表
     * @param storeProdId 档口商品ID
     * @return List<StoreProdColorSizeDTO>
     */
    List<StoreProdColorSizeDTO> selectListByStoreProdId(Long storeProdId);

}
