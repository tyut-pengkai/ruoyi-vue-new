package com.ruoyi.common.core.domain.vo.dictType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.annotation.Excel;
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
public class DictTypeVO {

    @ApiModelProperty(value =  "字典主键, 新增不传 编辑必传")
    private Long dictId;
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    private String remark;
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
