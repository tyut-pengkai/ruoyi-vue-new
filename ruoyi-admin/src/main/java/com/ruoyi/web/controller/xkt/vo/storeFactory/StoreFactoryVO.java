package com.ruoyi.web.controller.xkt.vo.storeFactory;

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
public class StoreFactoryVO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "档口工厂ID", notes = "新增不传 编辑必传")
    private Long storeFactoryId;
    @NotBlank(message = "工厂名称不能为空!")
    @ApiModelProperty(name = "工厂名称")
    private String facName;
    @ApiModelProperty(name = "工厂地址")
    private String facAddress;
    @ApiModelProperty(name = "工厂联系电话")
    private String facPhone;
    @ApiModelProperty("备注")
    private String remark;

}
