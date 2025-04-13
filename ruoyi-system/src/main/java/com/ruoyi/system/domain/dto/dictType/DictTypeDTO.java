package com.ruoyi.system.domain.dto.dictType;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统字典类型")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictTypeDTO {

    @ApiModelProperty(value =  "字典主键, 新增不传 编辑必传")
    private Long dictId;
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "字典描述")
    private String remark;
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
