package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySale;
import com.ruoyi.xkt.dto.dailySale.DailySaleDTO;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleMapper extends BaseMapper<DailySale> {

    /**
     * 查询指定日期销售数据
     *
     * @param voucherDate 凭证日期
     * @return List<DailySaleDTO>
     */
    List<DailySaleDTO> selectDailySale(Date voucherDate);

    /**
     * 筛选近一月销量过千的档口
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @return 销量过千的档口
     */
    List<DailyStoreTagDTO> selectSaleThousand(@Param("yesterday") Date yesterday, @Param("oneMonthAgo") Date oneMonthAgo);

    /**
     * 获取销量前十的档口
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 昨天往前推1个月
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> selectTop10List(@Param("yesterday") Date yesterday, @Param("oneMonthAgo") Date oneMonthAgo);

}
