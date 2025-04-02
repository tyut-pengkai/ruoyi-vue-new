package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.Date;

/**
 * 代发订单操作记录
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.615
 **/
@Data
public class StoreOrderOperationRecordDTO {
    /**
     * 代发订单操作记录ID
     */
    private Long id;
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
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
