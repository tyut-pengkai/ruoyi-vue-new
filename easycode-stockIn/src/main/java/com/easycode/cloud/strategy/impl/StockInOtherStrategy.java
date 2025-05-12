package com.easycode.cloud.strategy.impl;

import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.OrderStatusConstant;
import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.mapper.StockInOtherDetailMapper;
import com.easycode.cloud.mapper.StockInOtherMapper;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 其他入库策略
 * @author bcp
 */
@Component("11")
public class StockInOtherStrategy implements IRetTaskStrategy {
    @Autowired
    private StockInOtherMapper stockInOtherMapper;

    @Autowired
    private StockInOtherDetailMapper stockInOtherDetailMapper;
    @Override
    public int updateRetOrder(RetTaskDto retTaskDto) {
        //查询单据明细
        StockInOtherDetail detail = stockInOtherDetailMapper.selectStockInOtherDetailById(retTaskDto.getDetailId());
        //查询单据
        StockInOtherDto stockInOtherDto = new StockInOtherDto();
        stockInOtherDto.setOrderNo(detail.getOrderNo());
        List<StockInOther> stockInOthers = stockInOtherMapper.selectStockInOtherList(stockInOtherDto);
        StockInOther stockInOther = stockInOthers.get(0);
        if (stockInOtherDetailMapper.getBeContinuedNumber(stockInOther.getOrderNo(),retTaskDto.getTaskType()) == 0){
            stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        } else {
            stockInOther.setBillStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        }
        stockInOther.setUpdateBy(SecurityUtils.getUsername());
        stockInOther.setUpdateTime(DateUtils.getNowDate());
        return stockInOtherMapper.updateStockInOther(stockInOther);
    }
}
