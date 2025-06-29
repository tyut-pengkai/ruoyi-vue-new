package com.ruoyi.system.domain.dto.dictType;

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
public class DictTypeUpdateStatusDTO {

    @ApiModelProperty(value = "字典主键")
    private Long dictId;
    @ApiModelProperty(value = "字典状态 0正常 1停用")
    private Integer status;

}
