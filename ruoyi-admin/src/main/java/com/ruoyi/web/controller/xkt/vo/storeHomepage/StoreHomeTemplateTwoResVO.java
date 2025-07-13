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
@ApiModel("档口首页模板二返回数据")
@Data
public class StoreHomeTemplateTwoResVO {

    @ApiModelProperty(value = "顶部左侧轮播图")
    List<SHTOTopBannerVO> topLeftList;
    @ApiModelProperty(value = "右侧店铺公告")
    String notice;
    @ApiModelProperty(value = "店家推荐")
    List<StoreHomeTemplateItemResVO> recommendList;
    @ApiModelProperty(value = "人气爆款")
    List<StoreHomeTemplateItemResVO> popularSaleList;
    @ApiModelProperty(value = "当季新品")
    List<StoreHomeTemplateItemResVO> newProdList;

    @Data
    @ApiModel
    public static class SHTOTopBannerVO {
        @ApiModelProperty(value = "1.不跳转 为null 2.跳转店铺 为storeId 3.跳转商品 为storeProdId")
        private Long bizId;
        @ApiModelProperty(value = "1.不跳转 2.跳转店铺 3.跳转商品")
        private Integer jumpType;
        @ApiModelProperty(value = "跳转链接")
        private String fileUrl;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
