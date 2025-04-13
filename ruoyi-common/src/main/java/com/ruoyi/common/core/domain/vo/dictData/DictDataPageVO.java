package com.ruoyi.common.core.domain.vo.dictData;

import com.ruoyi.common.core.domain.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典明细类型分页查询入参")
@Data
public class DictDataPageVO extends BasePageVO {

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;
    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;

}
