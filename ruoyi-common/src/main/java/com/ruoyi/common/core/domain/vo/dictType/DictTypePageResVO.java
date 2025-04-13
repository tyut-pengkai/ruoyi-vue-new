package com.ruoyi.common.core.domain.vo.dictType;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("字典类型列表数据")
@Data
@Accessors(chain = true)
public class DictTypePageResVO {

    @ApiModelProperty(value = "字典类型ID")
    private Long dictId;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;

}
