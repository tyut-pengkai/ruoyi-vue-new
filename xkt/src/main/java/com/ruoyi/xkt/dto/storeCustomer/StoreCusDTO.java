package com.ruoyi.xkt.dto.storeCustomer;

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
@ApiModel("档口客户")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusDTO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(name = "客户名称")
    private String cusName;
    @ApiModelProperty(name = "客户联系电话")
    private String phone;
    @ApiModelProperty("备注")
    private String remark;

}
