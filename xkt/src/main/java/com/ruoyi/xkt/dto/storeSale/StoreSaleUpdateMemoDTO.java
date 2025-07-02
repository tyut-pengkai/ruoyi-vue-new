package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口销售修改备注")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleUpdateMemoDTO {

    @ApiModelProperty(value = "storeSaleId")
    private Long storeSaleId;
    @ApiModelProperty(value = "备注")
    private String remark;

}
