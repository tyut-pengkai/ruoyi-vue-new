package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageResDTO;

import java.util.List;

/**
 * 档口当前商品颜色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductColorMapper extends BaseMapper<StoreProductColor> {
    /**
     * 查询档口当前商品颜色
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 档口当前商品颜色
     */
    public StoreProductColor selectStoreProductColorByStoreProdColorId(Long storeProdColorId);

    /**
     * 查询档口当前商品颜色列表
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 档口当前商品颜色集合
     */
    public List<StoreProductColor> selectStoreProductColorList(StoreProductColor storeProductColor);

    /**
     * 新增档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    public int insertStoreProductColor(StoreProductColor storeProductColor);

    /**
     * 修改档口当前商品颜色
     *
     * @param storeProductColor 档口当前商品颜色
     * @return 结果
     */
    public int updateStoreProductColor(StoreProductColor storeProductColor);

    /**
     * 删除档口当前商品颜色
     *
     * @param storeProdColorId 档口当前商品颜色主键
     * @return 结果
     */
    public int deleteStoreProductColorByStoreProdColorId(Long storeProdColorId);

    /**
     * 批量删除档口当前商品颜色
     *
     * @param storeProdColorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductColorByStoreProdColorIds(Long[] storeProdColorIds);

    void updateDelFlagByStoreProdId(Long storeProdId);

    List<StoreProdColorDTO> selectListByStoreProdId(Long storeProdId);

    List<StoreProdPageResDTO> selectStoreProdColorPage(StoreProdPageDTO pageDTO);

}
