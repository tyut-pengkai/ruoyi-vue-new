package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStatistics;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.picture.ProductImgSearchCountDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductStatisticsMapper extends BaseMapper<StoreProductStatistics> {

    /**
     * 查询系统图搜热款
     *
     * @param beginDate 查询开始时间
     * @param endDate   查询结束时间
     * @return List<ProductImgSearchCountDTO>
     */
    List<ProductImgSearchCountDTO> listProdImgSearchCount(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 筛选近一月图搜前10的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> searchTop10Prod(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

    /**
     * 筛选近一月下载前10的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> downloadTop10Prod(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);


}
