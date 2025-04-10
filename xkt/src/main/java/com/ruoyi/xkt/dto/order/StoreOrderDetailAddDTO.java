package com.ruoyi.xkt.dto.order;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-10 23:06
 */
@Data
public class StoreOrderDetailAddDTO {
    /**
     * 商品颜色尺码ID
     */
    private Long storeProdColorSizeId;
    /**
     * 商品数量
     */
    private Integer goodsQuantity;
}
