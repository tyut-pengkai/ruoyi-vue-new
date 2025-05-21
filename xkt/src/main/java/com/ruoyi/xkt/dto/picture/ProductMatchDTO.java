package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMatchDTO {

    private Long storeProductId;

    private Float score;
}
