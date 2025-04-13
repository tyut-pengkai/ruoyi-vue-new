package com.ruoyi.system.domain.dto.dictData;

import com.ruoyi.common.core.domain.vo.BasePageVO;
import com.ruoyi.system.domain.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典明细类型分页查询入参")
@Data
public class DictDataPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;

}
