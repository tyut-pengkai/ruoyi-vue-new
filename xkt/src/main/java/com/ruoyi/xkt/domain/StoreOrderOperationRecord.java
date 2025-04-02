package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 代发订单操作记录
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.615
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderOperationRecord extends SimpleEntity {
    /**
     * 订单ID/订单明细ID，根据类型确定
     */
    private Long targetId;
    /**
     * 类型[1:订单 2:订单明细]
     */
    private Integer targetType;
    /**
     * 节点事件[1:下单 2:支付 3:取消 4:发货 5:完成 6:申请售后 7:寄回 8:售后拒绝 9:平台介入 10:售后完成]
     */
    private Integer action;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作时间
     */
    private Date operationTime;
    /**
     * 备注
     */
    private String remark;
}
