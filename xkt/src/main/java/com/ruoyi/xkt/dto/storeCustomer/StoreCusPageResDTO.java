package com.ruoyi.xkt.dto.storeCustomer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusPageResDTO {

    @ApiModelProperty(value = "档口客户ID")
    @JsonProperty("storeCusId")
    private Long id;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "客户联系电话")
    private String phone;
    @ApiModelProperty(value = "大小码加价 0 不加 1加价")
    private Integer addOverPrice;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
