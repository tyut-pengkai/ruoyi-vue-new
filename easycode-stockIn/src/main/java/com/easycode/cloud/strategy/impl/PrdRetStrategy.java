package com.easycode.cloud.strategy.impl;

import com.easycode.cloud.domain.dto.PrdReturnOrderDto;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.OrderStatusConstant;
import com.easycode.cloud.domain.PrdReturnOrder;
import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.mapper.PrdReturnOrderDetailMapper;
import com.easycode.cloud.mapper.PrdReturnOrderMapper;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 生产发料退货策略
 * @author bcp
 */
@Component("7")
public class PrdRetStrategy implements IRetTaskStrategy {

    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;

    @Autowired
    private PrdReturnOrderMapper prdReturnOrderMapper;
    
    @Override
    public int updateRetOrder(RetTaskDto retTaskDto) {
        //查询单据明细
        PrdReturnOrderDetail detail = prdReturnOrderDetailMapper.selectPrdReturnOrderDetailById(retTaskDto.getDetailId());
        //更新明细完成数量
        detail.setCompleteQty(retTaskDto.getQty().add(detail.getCompleteQty()));
        detail.setOperationCompleteQty(retTaskDto.getOperationCompleteQty().add(detail.getOperationCompleteQty()));
        detail.setUpdateBy(SecurityUtils.getUsername());
        detail.setUpdateTime(DateUtils.getNowDate());
        prdReturnOrderDetailMapper.updatePrdReturnOrderDetail(detail);
        //查询单据
        PrdReturnOrderDto returnOrderDto = new PrdReturnOrderDto();
        returnOrderDto.setReturnOrderNo(detail.getReturnOrderNo());
        List<PrdReturnOrder> prdReturnOrders = prdReturnOrderMapper.selectPrdReturnOrderList(returnOrderDto);
        PrdReturnOrder prdReturnOrder = prdReturnOrders.get(0);
        if (prdReturnOrderDetailMapper.getBeContinuedNumber(prdReturnOrder.getReturnOrderNo(),retTaskDto.getTaskType()) == 0){
            prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        } else {
            prdReturnOrder.setPrdOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        }
        prdReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        prdReturnOrder.setUpdateTime(DateUtils.getNowDate());
        // TODO 后续可能要添加 回写生产发料明细数量
        return prdReturnOrderMapper.updatePrdReturnOrder(prdReturnOrder);
    }
}
