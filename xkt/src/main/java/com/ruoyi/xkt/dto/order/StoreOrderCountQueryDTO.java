package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-07-23
 */
@Data
public class StoreOrderCountQueryDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 下单用户ID
     */
    private Long orderUserId;
    /**
     * 开始时间
     */
    private Date createTimeBegin;
    /**
     * 结束时间
     */
    private Date createTimeEnd;
    /**
     * 订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    private List<Integer> orderStatusList;
}
