package com.ruoyi.xkt.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangyq
 * @date 2025-05-23
 */
@Data
public class ProductImgSearchCountDTO implements Serializable {
    /**
     * 商品ID
     */
    private Long storeProductId;
    /**
     * 图搜次数
     */
    private Long imgSearchCount;
}
