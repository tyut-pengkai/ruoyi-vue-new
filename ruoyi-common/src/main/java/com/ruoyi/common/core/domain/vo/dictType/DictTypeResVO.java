package com.ruoyi.common.core.domain.vo.dictType;

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
@ApiModel("系统字典类型")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictTypeResVO {

    @ApiModelProperty(value = "字典主键, 新增不传 编辑必传")
    private Long dictId;
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    @ApiModelProperty(value = "字典描述")
    private String remark;
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
