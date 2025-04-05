package com.ruoyi.web.controller.xkt.vo.storeCustomer;

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
public class StoreCusVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID 新增为空，编辑必传")
    private Long storeCusId;
    @NotBlank(message = "客户名称不能为空!")
    @ApiModelProperty(value = "客户名称", required = true)
    private String cusName;
    @ApiModelProperty(value = "客户联系电话")
    private String phone;
    @ApiModelProperty(value = "备注")
    private String remark;

}
