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
     * 详情
     */
    private List<Item> items;

    @Data
    public static class Item {
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
         * 创建时间
         */
        private Date createTime;
    }
}
