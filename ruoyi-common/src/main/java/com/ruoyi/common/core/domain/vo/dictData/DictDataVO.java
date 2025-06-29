package com.ruoyi.common.core.domain.vo.dictData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统字典明细")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictDataVO {

    @ApiModelProperty(value = "新增不传,编辑必传")
    private Long dictDataId;
    @NotNull(message = "字典排序不能为空")
    @ApiModelProperty(value = "字典排序", required = true)
    private Long dictSort;
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 1, max = 100, message = "字典标签长度不能超过100个字符")
    @ApiModelProperty(value = "字典标签", required = true)
    private String dictLabel;
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 1, max = 100, message = "字典键值长度不能超过100个字符")
    @ApiModelProperty(value = "字典键值", required = true)
    private String dictValue;
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 1, max = 100, message = "字典类型长度不能超过100个字符")
    @ApiModelProperty(value = "字典类型", required = true)
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;

}
