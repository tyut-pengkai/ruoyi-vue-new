package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-03 13:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderAddResultDTO {

    private StoreOrder order;

    private List<StoreOrderDetail> orderDetails;
}
