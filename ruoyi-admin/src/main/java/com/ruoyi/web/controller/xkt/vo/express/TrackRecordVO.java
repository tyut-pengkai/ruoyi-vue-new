package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 物流轨迹记录
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.599
 **/
@ApiModel
@Data
public class TrackRecordVO {
    /**
     * 物流运单号
     */
    @ApiModelProperty(value = "物流运单号")
    private String expressWaybillNo;
    /**
     * 节点事件
     */
    @ApiModelProperty(value = "节点事件")
    private String action;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "时间")
    private Date createTime;
}
