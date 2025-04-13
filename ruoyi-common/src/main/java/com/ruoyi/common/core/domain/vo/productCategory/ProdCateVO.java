package com.ruoyi.common.core.domain.vo.productCategory;

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
@ApiModel("系统商品分类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdCateVO {

    @ApiModelProperty(value = "商品分类主键, 新增不传 编辑必传")
    private Long prodCateId;
    @ApiModelProperty(name = "分类名称")
    private String name;
    @ApiModelProperty(name = "分类名称")
    private Long parentId;
    @ApiModelProperty(name = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(name = "状态")
    private String status;
    @ApiModelProperty(name = "备注")
    private String remark;

}
