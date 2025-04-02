package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 代发订单物流轨迹
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderExpressTrack extends SimpleEntity {
    /**
     * 订单ID
     */
    private Long storeOrderId;
    /**
     * 订单明细ID
     */
    private Long storeOrderIdDetailId;
    /**
     * 排序
     */
    private Integer sort;
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
}
