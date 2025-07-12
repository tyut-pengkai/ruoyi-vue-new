package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeProductStock.*;

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
     * @param storeId          档口ID
     * @param storeProdStockId 档口商品库存主键
     * @return 档口商品库存
     */
    StoreProdStockResDTO selectByStoreProdStockId(Long storeId, Long storeProdStockId);

    /**
     * 增加库存
     *
     * @param storeId           档口ID
     * @param increaseStockList 增加库存入参
     * @return int
     */
    int increaseStock(Long storeId, List<StoreProdStockDTO> increaseStockList);

    /**
     * 减少库存
     *
     * @param storeId           档口ID
     * @param decreaseStockList 减少库存入参
     * @return int
     */
    int decreaseStock(Long storeId, List<StoreProdStockDTO> decreaseStockList);

    /**
     * 清空库存
     *
     * @param clearZeroDTO 清空库存dto
     * @return int
     */
    int clearStockToZero(StoreProdStockClearZeroDTO clearZeroDTO);

    /**
     * 直接调整库存
     *
     * @param storeId          档口ID
     * @param updateStockList  调整库存入参
     * @param multiplierFactor 乘积因子 0 直接调整库存，将库存更新为页面输入的数量 1 不变数量
     * @return int
     */
    int updateStock(Long storeId, List<StoreProdStockDTO> updateStockList, Integer multiplierFactor);

    /**
     * 查询档口商品分页
     *
     * @param pageDTO 分页入参
     * @return List<StoreProdStockPageResDTO>
     */
    Page<StoreProdStockPageResDTO> selectPage(StoreProdStockPageDTO pageDTO);

    /**
     * 根据档口ID和商品货号查询档口商品库存
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return String
     */
    List<StoreProdStockResDTO> selectByStoreIdAndProdArtNum(Long storeId, String prodArtNum);

    /**
     * 销售出库，输入货号，查询客户优惠信息及当前货号颜色的库存
     *
     * @param stockAndDiscountDTO 入参
     * @return StoreProdStockResDTO
     */
    StoreProdStockAndDiscountResDTO getStockAndCusDiscount(StoreProdStockAndDiscountDTO stockAndDiscountDTO);

    /**
     * 库存盘点
     *
     * @param storeId       档口ID
     * @param checkStockDTO 盘点入参
     * @return Integer
     */
    Integer checkAndUpdateStock(Long storeId, StoreProdCheckStockDTO checkStockDTO);

    /**
     * 导出库存明细
     *
     * @param exportDTO 导出入参
     * @return List<StoreProdStockDownloadDTO>
     */
    List<StoreProdStockDownloadDTO> export(StoreProdStockExportDTO exportDTO);

    /**
     * 查询APP库存列表
     *
     * @param pageDTO 入参
     * @return List<StoreProdStockPageResDTO>
     */
    Page<StoreProdStockAppPageResDTO> selectAppPage(StoreProdStockPageDTO pageDTO);
}
