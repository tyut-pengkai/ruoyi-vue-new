package com.easycode.cloud.strategy.impl;

import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.OrderStatusConstant;
import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.mapper.CostCenterReturnOrderDetailMapper;
import com.easycode.cloud.mapper.CostCenterReturnOrderMapper;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 成本中心退货策略
 * @author bcp
 */
@Component("8")
public class CostRetStrategy implements IRetTaskStrategy {

    @Autowired
    private CostCenterReturnOrderMapper costCenterReturnOrderMapper;
    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    @Override
    public int updateRetOrder(RetTaskDto retTaskDto) {
        //查询单据明细
        CostCenterReturnOrderDetail detail = costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(retTaskDto.getDetailId());
        //查询单据
        CostCenterReturnOrderVo returnOrderVo = new CostCenterReturnOrderVo();
        returnOrderVo.setOrderNo(detail.getReturnOrderNo());
        List<CostCenterReturnOrder> costCenterReturnOrders = costCenterReturnOrderMapper.selectCostCenterReturnOrderList(returnOrderVo);
        CostCenterReturnOrder costCenterReturnOrder = costCenterReturnOrders.get(0);
        if (costCenterReturnOrderDetailMapper.getBeContinuedNumber(costCenterReturnOrder.getOrderNo(),retTaskDto.getTaskType()) == 0){
            costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        } else {
            costCenterReturnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        }
        costCenterReturnOrder.setUpdateBy(SecurityUtils.getUsername());
        costCenterReturnOrder.setUpdateTime(DateUtils.getNowDate());
        return costCenterReturnOrderMapper.updateCostCenterReturnOrder(costCenterReturnOrder);
    }
}
