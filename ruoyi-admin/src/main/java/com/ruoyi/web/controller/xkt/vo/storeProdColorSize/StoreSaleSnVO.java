package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel(value = "档口销售条码查询model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleSnVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private String storeId;
    @NotNull(message = "档口客户ID不能为空!")
    @ApiModelProperty(value = "档口客户ID", required = true)
    private Long storeCusId;
    @NotNull(message = "是否退货不能为空!")
    @ApiModelProperty(value = "是否退货", required = true)
    private Boolean refund;
    @NotBlank(message = "条码不能为空!")
    @ApiModelProperty(value = "条码", required = true)
    private String sn;

}
