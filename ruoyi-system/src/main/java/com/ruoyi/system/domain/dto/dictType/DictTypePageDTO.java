package com.ruoyi.system.domain.dto.dictType;

import com.ruoyi.system.domain.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典类型分页查询入参")
@Data
public class DictTypePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;

}
