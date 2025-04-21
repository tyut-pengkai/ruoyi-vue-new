package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderRefund {

    private StoreOrder refundOrder;

    private List<StoreOrderDetail> refundOrderDetails;

    private StoreOrder originOrder;

}
