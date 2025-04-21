package com.ruoyi.xkt.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-14 18:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderCancelDTO {

    private Long storeOrderId;

    private Long operatorId;

    private String remark;
}
