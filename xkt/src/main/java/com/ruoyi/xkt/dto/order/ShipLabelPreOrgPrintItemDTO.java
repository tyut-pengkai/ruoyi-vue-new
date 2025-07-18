package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.Date;

/**
 * 面单预备原单号打印项
 *
 * @author liangyq
 * @date 2025-07-19
 **/
@Data
public class ShipLabelPreOrgPrintItemDTO {
    /**
     * 物流运单号（快递单号）
     */
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 物流名称
     */
    private String expressName;
    /**
     * 商品概要
     */
    private String goodsSummary;
    /**
     * 打印时间
     */
    private Date lastPrintTime;
}
