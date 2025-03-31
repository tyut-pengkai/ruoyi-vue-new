package com.ruoyi.xkt.dto.storeCustomer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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

    @ApiModelProperty(name = "档口客户ID", notes = "新增为空，编辑必传")
    @JsonProperty("storeCusId")
    private Long id;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @NotBlank(message = "客户名称不能为空!")
    @ApiModelProperty(name = "客户名称")
    private String cusName;
    @ApiModelProperty(name = "客户联系电话")
    private String phone;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
