package com.ruoyi.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.quartz.domain.DailySale;
import com.ruoyi.quartz.dto.DailySaleDTO;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleMapper extends BaseMapper<DailySale> {

    List<DailySaleDTO> selectDailySale(Date voucherDate);

}
