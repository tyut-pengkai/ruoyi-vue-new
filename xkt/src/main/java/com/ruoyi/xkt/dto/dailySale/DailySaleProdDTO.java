package com.ruoyi.xkt.dto.dailySale;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/4/16 23:06
 */
@Data
public class DailySaleProdDTO {

    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 档口商品货号
     */
    private String prodArtNum;
    /**
     * 销售金额
     */
    private BigDecimal saleAmount;
    /**
     * 退货金额
     */
    private BigDecimal returnAmount;
    /**
     * 销售数量
     */
    private Integer saleNum;
    /**
     * 退货数量
     */
    private Integer returnNum;

}
