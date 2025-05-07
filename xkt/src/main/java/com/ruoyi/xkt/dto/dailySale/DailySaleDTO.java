package com.ruoyi.xkt.dto.dailySale;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/4/16 23:06
 */
@Data
public class DailySaleDTO {

    /**
     * 档口ID
     */
    private Long storeId;

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
    /**
     * 拿货客户数量（人次）
     */
    private Integer customerNum;
    /**
     * 入库数量
     */
    private Integer storageNum;

}
