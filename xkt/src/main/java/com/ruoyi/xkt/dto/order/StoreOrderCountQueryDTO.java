package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.Date;

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
}
