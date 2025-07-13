package com.ruoyi.web.controller.xkt.vo.advertRound.pc.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class PCIndexMidSalesVO {

    @ApiModelProperty(value = "分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "销售榜分类明细列表")
    private List<PCIMSSaleVO> saleList;

    @Data
    public static class PCIMSSaleVO {
        @ApiModelProperty(value = "会员等级")
        private Integer memberLevel;
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "售价")
        private BigDecimal price;
        @ApiModelProperty(value = "销量")
        private Integer saleNum;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String mainPicUrl;
    }

}
