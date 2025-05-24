package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleProduct;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleProdDTO;
import com.ruoyi.xkt.dto.store.StoreIndexSaleTop10ResDTO;
import com.ruoyi.xkt.dto.store.StoreSaleTop10DTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleProductMapper extends BaseMapper<DailySaleProduct> {

    List<DailySaleProdDTO> selectDailySale(Date voucherDate);

    /**
     * 获取近一月销售榜数据
     *
     * @param oneMonthAgo 一月前
     * @param now         现在
     * @return List<CateSaleRankDTO>
     */
    List<CateSaleRankDTO> selectSaleRankList(@Param("oneMonthAgo") Date oneMonthAgo, @Param("now") Date now);

    /**
     * 获取档口首页商品销量前10
     * @param saleTop10DTO 查询入参
     * @return List<StoreIndexSaleTop10ResDTO>
     */
    List<StoreIndexSaleTop10ResDTO> selectTop10SaleList(StoreSaleTop10DTO saleTop10DTO);

}
