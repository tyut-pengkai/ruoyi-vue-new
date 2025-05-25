package com.ruoyi.common.core.domain.vo.productCategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统商品分类列表")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdCateListResVO {

    @ApiModelProperty(value = "商品分类主键, 新增不传 编辑必传")
    private Long prodCateId;
    @ApiModelProperty(value = "分类名称")
    private String name;
    @ApiModelProperty(value = "父级分类ID")
    private Long parentId;
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "图标url")
    private String iconUrl;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "子分类")
    private List<ProdCateListResVO> children;

}
