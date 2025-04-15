package com.ruoyi.common.core.domain.vo.productCategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统商品分类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdCateVO {

    @ApiModelProperty(value = "商品分类主键, 新增不传 编辑必传")
    private Long prodCateId;
    @ApiModelProperty(value = "分类名称")
    private String name;
    @ApiModelProperty(value = "分类名称")
    private Long parentId;
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;

}
