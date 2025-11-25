package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.order.StoreOrderDetailInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface StoreOrderDetailMapper extends BaseMapper<StoreOrderDetail> {

    List<StoreOrderDetailInfoDTO> listInfoByStoreOrderIds(@Param("storeOrderIds") Collection<Long> storeOrderIds);

    List<Long> listRefundOrderDetailOriginIds(@Param("storeOrderId")Long storeOrderId);
}
