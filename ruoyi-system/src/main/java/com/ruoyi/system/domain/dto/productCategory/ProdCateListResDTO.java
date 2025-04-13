package com.ruoyi.system.domain.dto.productCategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class ProdCateListResDTO {

    @ApiModelProperty(value = "商品分类主键")
    @JsonProperty(value = "prodCateId")
    private Long id;
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
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "子分类")
    private List<ProdCateListResDTO> children;

}
