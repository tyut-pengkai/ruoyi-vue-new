package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 物流轨迹
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressTrackRecord extends SimpleEntity {
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
}
