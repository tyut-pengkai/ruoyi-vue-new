package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.enums.EExpressStatus;
import lombok.Data;

import java.util.Date;

/**
 * 代发订单物流轨迹
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@Data
public class StoreOrderExpressTrackAddDTO {
    /**
     * 物流运单号（快递单号）
     */
    private String expressWaybillNo;
    /**
     * 节点事件
     */
    private String action;
    /**
     * 描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 物流状态
     */
    private EExpressStatus expressStatus;
}
