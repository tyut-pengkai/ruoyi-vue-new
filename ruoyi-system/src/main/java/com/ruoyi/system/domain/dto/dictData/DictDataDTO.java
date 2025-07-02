package com.ruoyi.system.domain.dto.dictData;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统字典明细")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictDataDTO {

    @ApiModelProperty(value = "新增不传,编辑必传")
    private Long dictDataId;
    @ApiModelProperty(value = "字典排序")
    private Long dictSort;
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;
    @ApiModelProperty(value = "字典键值")
    private String dictValue;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;

}
