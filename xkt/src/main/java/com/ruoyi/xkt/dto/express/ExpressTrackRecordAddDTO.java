package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流轨迹
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpressTrackRecordAddDTO {
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 物流运单号
     */
    private String expressWaybillNo;
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
