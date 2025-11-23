package com.ruoyi.web.controller.xkt.vo.elasticSearch;

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
public class EsProdBatchDeleteVO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "商品ID列表不能为空")
    @ApiModelProperty(value = "商品ID列表", required = true)
    private List<Long> storeProdIdList;

}
