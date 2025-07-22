package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleProduct;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleProdDTO;
import com.ruoyi.xkt.dto.dailyStoreProd.DailyStoreProdSaleDTO;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.store.StoreIndexSaleTop10ResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleProductMapper extends BaseMapper<DailySaleProduct> {

    /**
     * 查询档口商品销售数据
     *
     * @param voucherDate 昨天
     * @return List<DailySaleProdDTO>
     */
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
     *
     * @param storeId          storeId
     * @param voucherDateStart 开始时间
     * @param voucherDateEnd   结束时间
     * @return List<StoreIndexSaleTop10ResDTO>
     */
    List<StoreIndexSaleTop10ResDTO> selectTop10SaleList(@Param("storeId") Long storeId,
                                                        @Param("voucherDateStart") String voucherDateStart,
                                                        @Param("voucherDateEnd") String voucherDateEnd);

    /**
     * 获取爆款频出的前50商品及档口
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 昨天往前推1个月
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> selectTop50ProdList(@Param("yesterday") Date yesterday, @Param("oneMonthAgo") Date oneMonthAgo);

    /**
     * 筛选销量超过1000的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> prodSale1000List(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

    /**
     * 筛选销量前十的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> prodSaleTop10List(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

    /**
     * 筛选销量前100的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreProdSaleDTO>
     */
    List<DailyStoreProdSaleDTO> prodSaleTop50List(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

    /**
     * 每一个分类销量排名
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreProdSaleDTO>
     */
    List<DailyStoreProdSaleDTO> prodCateSaleTop50List(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

}
