package com.ruoyi.web.controller.xkt.vo.storeHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页模板一返回数据")
@Data
public class StoreHomeTemplateOneResVO {

    @ApiModelProperty(value = "顶部左侧轮播图")
    List<SHTOTopBannerVO> topLeftList;
    @ApiModelProperty(value = "顶部右侧商品")
    List<StoreHomeTemplateItemResVO> topRightList;
    @ApiModelProperty(value = "店家推荐")
    List<StoreHomeTemplateItemResVO> recommendList;
    @ApiModelProperty(value = "人气爆款")
    List<StoreHomeTemplateItemResVO> popularSaleList;
    @ApiModelProperty(value = "当季新品")
    List<StoreHomeTemplateItemResVO> newProdList;
    @ApiModelProperty(value = "销量排行")
    List<StoreHomeTemplateItemResVO> saleRankList;

    @Data
    public static class SHTOTopBannerVO {
        @ApiModelProperty(value = "storeProdId")
        private Long storeProdId;
        @ApiModelProperty(value = "1.档口（推广图） 2.商品  10.不跳转")
        private Integer displayType;
        @ApiModelProperty(value = "跳转链接")
        private String fileUrl;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
