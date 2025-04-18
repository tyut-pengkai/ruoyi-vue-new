package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 档口商品库存Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStockMapper extends BaseMapper<StoreProductStock> {
    /**
     * 查询档口商品库存
     *
     * @param id 档口商品库存主键
     * @return 档口商品库存
     */
    public StoreProductStock selectStoreProductStockByStoreProdStockId(Long id);

    /**
     * 查询档口商品库存列表
     *
     * @param storeProductStock 档口商品库存
     * @return 档口商品库存集合
     */
    public List<StoreProductStock> selectStoreProductStockList(StoreProductStock storeProductStock);

    /**
     * 新增档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    public int insertStoreProductStock(StoreProductStock storeProductStock);

    /**
     * 修改档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    public int updateStoreProductStock(StoreProductStock storeProductStock);

    /**
     * 删除档口商品库存
     *
     * @param id 档口商品库存主键
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockId(Long id);

    /**
     * 批量删除档口商品库存
     *
     * @param storeProdStockIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockIds(Long[] storeProdStockIds);

    /**
     * 档口商品库存分页查询
     * @param pageDTO 查询参数
     * @return 结果
     */
    List<StoreProdStockPageResDTO> selectStockPage(StoreProdStockPageDTO pageDTO);

    /**
     * 筛选库存前10的档口
     * @param yesterday 昨天
     * @param oneMonthAgo 一月前
     * @return
     */
    List<DailyStoreTagDTO> selectTop10List(@Param("yesterday") Date yesterday,@Param("oneMonthAgo") Date oneMonthAgo);
}
