package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.dto.dailySale.WeekCateSaleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface WeekCateSaleMapper {

    List<WeekCateSaleDTO> selectWeekCateSale(@Param("yesterday") Date yesterday, @Param("pastDate") Date pastDate);
}
