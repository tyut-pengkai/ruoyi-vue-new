package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleProduct;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleProdDTO;
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

}
