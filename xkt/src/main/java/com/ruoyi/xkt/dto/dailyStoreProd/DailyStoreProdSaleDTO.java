package com.ruoyi.xkt.dto.dailyStoreProd;

import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/4/16 23:06
 */
@Data
public class DailyStoreProdSaleDTO {

    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 商品分类ID
     */
    private Long prodCateId;
    /**
     * 商品分类名称
     */
    private String prodCateName;
    /**
     * 销售数量
     */
    private Integer count;

}
