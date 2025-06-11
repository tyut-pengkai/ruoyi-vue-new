package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockDownloadDTO;
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
     * 档口商品库存分页查询
     *
     * @param pageDTO 查询参数
     * @return 结果
     */
    List<StoreProdStockPageResDTO> selectStockPage(StoreProdStockPageDTO pageDTO);

    /**
     * 筛选库存前10的档口
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> selectTop10List(@Param("yesterday") Date yesterday, @Param("oneMonthAgo") Date oneMonthAgo);

    /**
     * 筛选库存导出列表
     *
     * @param storeProdStockIdList 档口商品库存ID列表
     * @return List<StoreProdStockDownloadDTO>
     */
    List<StoreProdStockDownloadDTO> selectExportList(@Param("storeProdStockIdList") List<Long> storeProdStockIdList);

    /**
     * 筛选库存所有列表
     *
     * @return List<StoreProdStockDownloadDTO>
     */
    List<StoreProdStockDownloadDTO> selectAllStockList();
}
