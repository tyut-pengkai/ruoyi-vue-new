package com.ruoyi.xkt.dto.storeCustomer;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "客户联系电话")
    private String phone;
    @ApiModelProperty(value = "大小码加价 0 不加 1加价")
    private Integer addOverPrice;
    @ApiModelProperty("备注")
    private String remark;

}
