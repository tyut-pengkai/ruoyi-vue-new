package com.easycode.cloud.strategy.impl.vh.returnSubmit;

import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderDetailMapper;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderMapper;
import com.easycode.cloud.strategy.IRetTaskStrategy;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.OrderStatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 原材料委外发退货策略
 * @author gf
 */
@Component("14")
public class RwmOutsourceStrategy implements IRetTaskStrategy {

    @Autowired
    private RwmOutSourceReturnOrderDetailMapper detailMapper;

    @Autowired
    private RwmOutSourceReturnOrderMapper orderMapper;

    @Override
    public int updateRetOrder(RetTaskDto retTaskDto) {
        //查询单据明细
        RwmOutSourceReturnOrderDetail detail = detailMapper.selectRwmOutSourceReturnOrderDetailById(retTaskDto.getDetailId());
        //更新明细完成数量
        detail.setReturnQty(retTaskDto.getQty().add(detail.getReturnQty()));
        detail.setOperationQty(retTaskDto.getOperationCompleteQty().add(detail.getOperationQty()));
        detail.setUpdateBy(SecurityUtils.getUsername());
        detail.setUpdateTime(DateUtils.getNowDate());
        detailMapper.updateRwmOutSourceReturnOrderDetail(detail);
        //查询单据
        RwmOutSourceReturnOrderVo returnOrderDto = new RwmOutSourceReturnOrderVo();
        returnOrderDto.setOrderNo(detail.getReturnOrderNo());
        List<RwmOutSourceReturnOrder> returnOrderList = orderMapper.selectRwmOutSourceReturnOrderList(returnOrderDto);
        RwmOutSourceReturnOrder returnOrder = returnOrderList.get(0);
        if (detailMapper.getBeContinuedNumber(returnOrder.getOrderNo(),retTaskDto.getTaskType()) == 0){
            returnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_COMPLETE);
        } else {
            returnOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_PART_COMPLETE);
        }
        returnOrder.setUpdateBy(SecurityUtils.getUsername());
        returnOrder.setUpdateTime(DateUtils.getNowDate());
        return orderMapper.updateRwmOutSourceReturnOrder(returnOrder);
    }
}
