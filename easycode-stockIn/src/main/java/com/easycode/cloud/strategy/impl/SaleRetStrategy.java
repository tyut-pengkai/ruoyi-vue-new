package com.easycode.cloud.strategy.impl;

import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.OrderStatusConstant;
import com.weifu.cloud.constant.StockInTaskConstant;
import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.SaleReturnOrderVo;
import com.easycode.cloud.mapper.SaleReturnOrderDetailMapper;
import com.easycode.cloud.mapper.SaleReturnOrderMapper;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 销售发货退货策略
 * @author gf
 */
@Component("13")
public class SaleRetStrategy implements IRetTaskStrategy {

    @Autowired
    private SaleReturnOrderDetailMapper orderDetailMapper;

    @Autowired
    private SaleReturnOrderMapper orderMapper;

    @Override
    public int updateRetOrder(RetTaskDto retTaskDto) {
        // 查询明细
        SaleReturnOrderVo saleReturnOrderVo = new SaleReturnOrderVo();
        saleReturnOrderVo.setOrderNo(retTaskDto.getStockinOrderNo());
        List<SaleReturnOrder> saleReturnOrderList = orderMapper.selectSaleReturnOrderList(saleReturnOrderVo);
        SaleReturnOrder saleReturnOrder = saleReturnOrderList.get(0);
        if (orderDetailMapper.getBeContinuedNumber(retTaskDto.getStockinOrderNo(), StockInTaskConstant.SALE_RETURN_TYPE) == 0){
            saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
            retTaskDto.setSubmit(true);
        } else {
            // 部分完成还不能过账
            retTaskDto.setSubmit(false);
            saleReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        }
        saleReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        saleReturnOrder.setUpdateTime(DateUtils.getNowDate());
        retTaskDto.setSaleCode(saleReturnOrder.getSaleCode());
        return orderMapper.updateSaleReturnOrder(saleReturnOrder);
    }
}
