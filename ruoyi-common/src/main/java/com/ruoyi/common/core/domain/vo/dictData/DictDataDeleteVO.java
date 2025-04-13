package com.ruoyi.common.core.domain.vo.dictData;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("删除系统字典明细类型")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictDataDeleteVO {

    @NotNull(message = "字典明细主键列表不能为空")
    @ApiModelProperty(value = "字典明细主键列表")
    private List<Long> dictDataIdList;

}
