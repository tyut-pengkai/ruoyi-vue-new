package com.ruoyi.web.controller.xkt.vo.storeCustomer;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确，请输入有效的中国大陆手机号")
    private String phone;
    @NotNull(message = "大小码加价不能为空!")
    @ApiModelProperty(value = "大小码加价 0 不加 1加价", required = true)
    private Integer addOverPrice;
    @ApiModelProperty(value = "备注")
    private String remark;

}
