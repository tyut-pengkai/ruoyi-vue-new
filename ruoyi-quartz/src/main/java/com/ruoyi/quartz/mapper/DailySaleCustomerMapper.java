package com.ruoyi.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.quartz.domain.DailySaleCustomer;
import com.ruoyi.quartz.dto.DailySaleCusDTO;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleCustomerMapper extends BaseMapper<DailySaleCustomer> {

    List<DailySaleCusDTO> selectDailySale(Date voucherDate);

}
