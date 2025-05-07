package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleCustomer;
import com.ruoyi.xkt.dto.dailySale.DailySaleCusDTO;

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
