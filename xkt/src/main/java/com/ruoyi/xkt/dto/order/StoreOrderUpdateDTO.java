package com.ruoyi.xkt.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-04-10 22:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderUpdateDTO extends StoreOrderAddDTO {
    /**
     * 订单ID
     */
    private Long id;
}
