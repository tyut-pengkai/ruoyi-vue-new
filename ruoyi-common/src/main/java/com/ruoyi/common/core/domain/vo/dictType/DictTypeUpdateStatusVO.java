package com.ruoyi.common.core.domain.vo.dictType;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("停用/启用类型状态")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictTypeUpdateStatusVO {

    @NotNull(message = "字典主键不能为空")
    @ApiModelProperty(value = "字典主键", required = true)
    private Long dictId;
    @NotNull(message = "字典状态不能为空!")
    @ApiModelProperty(value = "字典状态 0正常 1停用", required = true)
    private Integer status;

}
