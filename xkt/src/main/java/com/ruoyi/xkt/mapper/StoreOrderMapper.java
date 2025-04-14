package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.StoreOrderPageItemDTO;
import com.ruoyi.xkt.dto.order.StoreOrderQueryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface StoreOrderMapper extends BaseMapper<StoreOrder> {

    List<StoreOrderPageItemDTO> listStoreOrderPageItem(StoreOrderQueryDTO queryDTO);
}
