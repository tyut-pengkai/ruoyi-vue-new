package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口销售修改备注")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleUpdateMemoVO {

    @NotNull(message = "storeSaleId不能为空!")
    @ApiModelProperty(value = "storeSaleId")
    private Long storeSaleId;
    @NotBlank(message = "备注不能为空!")
    @ApiModelProperty(value = "备注")
    @Size(min = 0, max = 100, message = "备注不能超过100字!")
    private String remark;

}
