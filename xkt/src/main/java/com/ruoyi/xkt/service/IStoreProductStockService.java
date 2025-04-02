package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageResDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockResDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockUpdateDTO;

import java.util.List;

/**
 * 档口商品库存Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStockService {
    /**
     * 查询档口商品库存
     *
     *
     * @param storeId 档口ID
     * @param storeProdStockId 档口商品库存主键
     * @return 档口商品库存
     */
    public StoreProdStockResDTO selectByStoreProdStockId(Long storeId, Long storeProdStockId);

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
     * 批量删除档口商品库存
     *
     * @param storeProdStockIds 需要删除的档口商品库存主键集合
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockIds(Long[] storeProdStockIds);

    /**
     * 删除档口商品库存信息
     *
     * @param storeProdStockId 档口商品库存主键
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockId(Long storeProdStockId);

    /**
     * 增加库存
     * @param storeId 档口ID
     * @param increaseStockList 增加库存入参
     * @return int
     */
    int increaseStock(Long storeId, List<StoreProdStockUpdateDTO> increaseStockList);

    /**
     * 减少库存
     * @param storeId 档口ID
     * @param decreaseStockList 减少库存入参
     * @return int
     */
    int decreaseStock(Long storeId, List<StoreProdStockUpdateDTO> decreaseStockList);

    /**
     * 清空库存
     * @param storeId 档口ID
     * @param storeProdStockId 清空库存
     * @return int
     */
    int clearStockToZero(Long storeId, Long storeProdStockId);

    /**
     * 直接调整库存
     * @param storeId 档口ID
     * @param updateStockList 调整库存入参
     * @param multiplierFactor 乘积因子 0 直接调整库存，将库存更新为页面输入的数量 1 不变数量
     * @return int
     */
    int updateStock(Long storeId, List<StoreProdStockUpdateDTO> updateStockList, Integer multiplierFactor);

    /**
     * 查询档口商品分页
     * @param pageDTO 分页入参
     * @return List<StoreProdStockPageResDTO>
     */
    Page<StoreProdStockPageResDTO> selectPage(StoreProdStockPageDTO pageDTO);

    /**
     * 根据档口ID和商品货号查询档口商品库存
     * @param storeId 档口ID
     * @param prodArtNum 商品货号
     * @return String
     */
    List<StoreProdStockResDTO> selectByStoreIdAndProdArtNum(Long storeId, String prodArtNum);
}
