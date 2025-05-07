package com.ruoyi.xkt.dto.website;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("网站首页搜索")
@Data
public class IndexSearchDTO extends BasePageDTO {

    @ApiModelProperty(value = "搜索内容")
    private String search;
    @ApiModelProperty(value = "商品状态列表")
    private List<String> prodStatusList;
    @ApiModelProperty(value = "一级类目ID列表")
    private List<String> parCateIdList;
    @ApiModelProperty(value = "二级类目ID列表")
    private List<String> prodCateIdList;
    @ApiModelProperty(value = "最小价格")
    private String minPrice;
    @ApiModelProperty(value = "最大价格")
    private String maxPrice;
    @ApiModelProperty(value = "风格列表")
    private List<String> styleList;
    @ApiModelProperty(value = "季节列表")
    private List<String> seasonList;
    @ApiModelProperty(value = "排序")
    private String sort;

}
