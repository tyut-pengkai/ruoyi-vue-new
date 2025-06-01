package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 物流轨迹查询
 *
 * @author liangyq
 * @date 2025-05-14
 **/
@ApiModel
@Data
public class TrackRecordQueryVO {

    @NotEmpty(message = "物流运单号不能为空")
    @ApiModelProperty(value = "物流运单号集合")
    private List<String> expressWaybillNos;
}
