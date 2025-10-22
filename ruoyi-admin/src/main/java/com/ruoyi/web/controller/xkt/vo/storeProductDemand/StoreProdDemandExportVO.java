package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreProdDemandExportVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "需求状态 1 待生产 2 生产中 3 生产完成")
    private Integer detailStatus;
    @ApiModelProperty(value = "档口需求明细ID")
    private List<Long> storeProdDemandDetailIdList;

}
