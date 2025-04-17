package com.ruoyi.quartz.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/4/16 23:06
 */
@Data
public class DailySaleCusDTO {

    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口客户ID
     */
    private Long storeCusId;
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
