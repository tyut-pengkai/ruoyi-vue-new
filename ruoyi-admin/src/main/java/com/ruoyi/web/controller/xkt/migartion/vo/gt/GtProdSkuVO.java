package com.ruoyi.web.controller.xkt.migartion.vo.gt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class GtProdSkuVO {

    private Integer product_id;
    private String article_number;
    private Integer category_nid;
    private String color;
    private Integer size;
    private String characters;
    private BigDecimal weight;
    private BigDecimal price;

}
