package com.ruoyi.system.domain.dto.productCategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统商品分类列表")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdCateListDTO {

    @ApiModelProperty(name = "分类名称")
    private String name;
    @ApiModelProperty(name = "状态")
    private String status;

}
