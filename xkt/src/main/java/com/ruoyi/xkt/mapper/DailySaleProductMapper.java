package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleProduct;
import com.ruoyi.xkt.dto.dailySale.DailySaleProdDTO;

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
