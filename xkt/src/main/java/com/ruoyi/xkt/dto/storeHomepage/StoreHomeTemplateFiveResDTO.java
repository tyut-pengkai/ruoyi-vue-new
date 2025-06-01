package com.ruoyi.xkt.dto.storeHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页模板五返回数据")
@Data
@Accessors(chain = true)
public class StoreHomeTemplateFiveResDTO {

    @ApiModelProperty(value = "顶部左侧轮播图")
    List<StoreHomeTopBannerResDTO> topLeftList;
    @ApiModelProperty(value = "顶部右侧商品")
    List<StoreHomeTemplateItemResDTO> topRightList;
    @ApiModelProperty(value = "右侧店铺公告")
    String notice;
    @ApiModelProperty(value = "店家推荐")
    List<StoreHomeTemplateItemResDTO> recommendList;
    @ApiModelProperty(value = "销量排行")
    List<StoreHomeTemplateItemResDTO> saleRankList;

}
