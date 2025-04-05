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
@ApiModel("档口需求删除")
@Data
public class StoreProdDemandDeleteVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @NotNull(message = "档口需求明细ID不能为空")
    @ApiModelProperty(value = "档口需求明细ID", required = true)
    private List<Long> storeProdDemandDetailIdList;

}
