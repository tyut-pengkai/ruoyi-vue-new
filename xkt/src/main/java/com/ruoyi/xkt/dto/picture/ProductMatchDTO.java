package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liangyq
 * @date 2025-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMatchDTO implements Serializable {
    /**
     * 商品ID
     */
    private Long storeProductId;
    /**
     * 图片匹配分
     */
    private Float score;
}
