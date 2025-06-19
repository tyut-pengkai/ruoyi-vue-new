package com.ruoyi.web.controller.xkt.vo.website;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("网站首页搜索")
@Data
public class IndexSearchVO extends BasePageVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
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
    @NotNull(message = "排序方式不能为空")
    @ApiModelProperty(value = "排序方式", required = true)
    private SortOrder order;


}
