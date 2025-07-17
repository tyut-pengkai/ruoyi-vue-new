package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流轨迹
 *
 * @author liangyq
 * @date 2025-07-17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackRecordDTO implements Serializable {
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
