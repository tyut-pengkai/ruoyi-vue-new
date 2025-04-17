package com.ruoyi.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.quartz.domain.DailySaleCustomer;
import com.ruoyi.quartz.domain.DailySaleProduct;
import com.ruoyi.quartz.dto.DailySaleDTO;
import com.ruoyi.quartz.dto.DailySaleProdDTO;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleProductMapper extends BaseMapper<DailySaleProduct> {

    List<DailySaleProdDTO> selectDailySale(Date voucherDate);

}
