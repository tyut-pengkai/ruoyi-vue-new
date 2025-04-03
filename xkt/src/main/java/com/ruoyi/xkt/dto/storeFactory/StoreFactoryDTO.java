package com.ruoyi.xkt.dto.storeFactory;

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
@ApiModel("档口工厂")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreFactoryDTO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口工厂ID")
    private Long storeFactoryId;
    @ApiModelProperty(name = "工厂名称")
    private String facName;
    @ApiModelProperty(name = "工厂地址")
    private String facAddress;
    @ApiModelProperty(name = "工厂联系电话")
    private String facPhone;
    @ApiModelProperty("备注")
    private String remark;

}
