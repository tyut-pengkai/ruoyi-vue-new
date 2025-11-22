package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface StoreOrderMapper extends BaseMapper<StoreOrder> {

    List<StoreOrderPageItemDTO> listStoreOrderPageItem(StoreOrderQueryDTO queryDTO);

    List<StoreOrder> listNeedContinueRefundOrder();

    StoreOrderCountDTO countOrder(StoreOrderCountQueryDTO queryDTO);

    List<StoreOrder> listNeedAutoCompleteOrder(@Param("deliveryBeforeTime") Date deliveryBeforeTime);

    List<StoreOrder> listNeedAutoRefundOrder();

    List<StoreOrder> listNeedAutoCheckCompletePendingShipmentOrders();

    List<StoreOrderDetailStatusDTO> listOrderDetailStatusByOrderIds(@Param("orderIds") Collection<Long> orderIds);
}
