package com.ruoyi.xkt.dto.express;

import lombok.Data;

import java.util.Date;

/**
 * 物流轨迹
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@Data
public class ExpressTrackRecordDTO {
    /**
     * 订单物流明细ID
     */
    private Long id;
    /**
     * 物流运单号
     */
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    private Long expressId;
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
