package com.ruoyi.system.domain.dto.dictType;

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
@ApiModel("删除系统字典类型")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictTypeDeleteDTO {

    @ApiModelProperty(value = "字典主键列表")
    private List<Long> dictIdList;

}
