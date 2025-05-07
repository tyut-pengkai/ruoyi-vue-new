package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySale;
import com.ruoyi.xkt.dto.dailySale.DailySaleDTO;

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
