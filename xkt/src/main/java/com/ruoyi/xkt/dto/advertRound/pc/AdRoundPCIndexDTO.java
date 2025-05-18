package com.ruoyi.xkt.dto.advertRound.pc;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC首页广告数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundPCIndexDTO {

    @ApiModelProperty(value = "顶部横向轮播图")
    private List<ARPCITopLeftBannerDTO> topLeftList;
    @ApiModelProperty(value = "顶部纵向轮播图")
    private List<ARPCITopRightBannerDTO> topRightList;

    @ApiModelProperty(value = "销售榜榜一分类ID")
    private Long saleOneCateId;
    @ApiModelProperty(value = "销售榜榜一分类名称")
    private String saleOneCateName;
    @ApiModelProperty(value = "销售榜榜一")
    private List<ARPCISaleDTO> saleOneList;
    @ApiModelProperty(value = "销售榜榜二分类ID")
    private Long saleTwoCateId;
    @ApiModelProperty(value = "销售榜榜二分类名称")
    private String saleTwoCateName;
    @ApiModelProperty(value = "销售榜榜二")
    private List<ARPCISaleDTO> saleTwoList;
    @ApiModelProperty(value = "销售榜榜三分类ID")
    private Long saleThreeCateId;
    @ApiModelProperty(value = "销售榜榜三分类名称")
    private String saleThreeCateName;
    @ApiModelProperty(value = "销售榜榜三")
    private List<ARPCISaleDTO> saleThreeList;
    @ApiModelProperty(value = "销售榜榜四分类ID")
    private Long saleFourthCateId;
    @ApiModelProperty(value = "销售榜榜四分类名称")
    private String saleFourthCateName;
    @ApiModelProperty(value = "销售榜榜四 ")
    private List<ARPCISaleDTO> saleFourthList;

    @ApiModelProperty(value = "人气榜左轮播图")
    private List<ARPCIPopBannerDTO> popLeftList;
    @ApiModelProperty(value = "人气榜中部图")
    private List<ARPCIPopMidDTO> popMidList;
    @ApiModelProperty(value = "人气榜右侧图")
    private List<ARPCIPopRightDTO> popRightList;

    @ApiModelProperty(value = "首页底部横幅列表")
    private List<ARPCIBottomBannerDTO> bottomBannerList;


    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "顶部横向轮播图")
    public static class ARPCITopLeftBannerDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "推广图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "顶部纵向商品")
    @Accessors(chain = true)
    public static class ARPCITopRightBannerDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "销售榜")
    public static class ARPCISaleDTO {
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
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "人气榜左轮播图")
    public static class ARPCIPopBannerDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "人气榜中部图")
    public static class ARPCIPopMidDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "人气榜右侧图")
    public static class ARPCIPopRightDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "首页横幅")
    public static class ARPCIBottomBannerDTO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }


}
