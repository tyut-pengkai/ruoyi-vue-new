package com.ruoyi.xkt.dto.express;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 代发订单物流轨迹
 *
 * @author liangyq
 * @date 2025-05-07 11:57:52.599
 **/
@Data
public class ExpressTrackDTO {
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
     * 记录
     */
    private List<ExpressTrackRecordDTO> records;
    /**
     * 创建时间
     */
    private Date createTime;
}
