package com.ruoyi.system.domain.dto.productCategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("APP首页商品分类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class AppHomeProdCateListResDTO {

    @ApiModelProperty(value = "商品分类主键")
    private Long id;
    @ApiModelProperty(value = "分类名称")
    private String name;
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "图标url")
    private String iconUrl;

}
