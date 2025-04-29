package com.ruoyi.system.domain.dto.dictData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统字典明细")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictDataResDTO {

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
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
