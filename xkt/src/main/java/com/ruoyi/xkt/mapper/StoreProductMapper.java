package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeResDTO;
import com.ruoyi.xkt.dto.storeProduct.ProductESDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdFuzzyResPicDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdStatusCountDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口商品Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductMapper extends BaseMapper<StoreProduct> {
    /**
     * 查询档口商品
     *
     * @param id 档口商品主键
     * @return 档口商品
     */
    public StoreProduct selectStoreProductByStoreProdId(Long id);

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品集合
     */
    public List<StoreProduct> selectStoreProductList(StoreProduct storeProduct);

    /**
     * 新增档口商品
     *
     * @param storeProduct 档口商品
     * @return 结果
     */
    public int insertStoreProduct(StoreProduct storeProduct);

    /**
     * 修改档口商品
     *
     * @param storeProduct 档口商品
     * @return 结果
     */
    public int updateStoreProduct(StoreProduct storeProduct);

    /**
     * 删除档口商品
     *
     * @param id 档口商品主键
     * @return 结果
     */
    public int deleteStoreProductByStoreProdId(Long id);

    /**
     * 批量删除档口商品
     *
     * @param storeProdIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductByStoreProdIds(Long[] storeProdIds);

    /**
     * 根据商品货号模糊查询档口商品并返回商品主图
     *
     * @param storeId    档口ID
     * @param prodArtNum 货号
     * @return List<StoreProdFuzzyResPicDTO>
     */
    List<StoreProdFuzzyResPicDTO> fuzzyQueryResPicList(@Param("storeId") Long storeId, @Param("prodArtNum") String prodArtNum);

    /**
     * 查询档口的在售、尾货、下架数量
     * @param storeId 档口ID
     * @return StoreProdCountDTO
     */
    StoreProdStatusCountDTO selectStatusCount(Long storeId);

    /**
     * 档口商品ID列表
     * @param idList id列表
     * @return List<ProductESDTO>
     */
    List<ProductESDTO> selectESDTOList(@Param("idList") List<Long> idList);

}
