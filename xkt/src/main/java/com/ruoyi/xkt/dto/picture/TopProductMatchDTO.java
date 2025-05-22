package com.ruoyi.xkt.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangyq
 * @date 2025-05-22
 */
@Data
public class TopProductMatchDTO implements Serializable {
    /**
     * 商品ID
     */
    private Long storeProductId;
    /**
     * 图搜次数
     */
    private Long imgSearchCount;
    /**
     * 同款商品数
     */
    private Integer sameProductCount;
}
