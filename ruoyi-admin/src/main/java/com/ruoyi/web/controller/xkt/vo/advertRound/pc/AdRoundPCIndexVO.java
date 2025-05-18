package com.ruoyi.web.controller.xkt.vo.advertRound.pc;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel("PC 首页 顶部横向轮播图")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundPCIndexVO {

    @ApiModelProperty(value = "顶部横向轮播图")
    private List<ARPCITopLeftBannerVO> topLeftList;
    @ApiModelProperty(value = "顶部纵向轮播图")
    private List<ARPCITopRightBannerVO> topRightList;

    @ApiModelProperty(value = "销售榜榜一分类ID")
    private Long saleOneCateId;
    @ApiModelProperty(value = "销售榜榜一分类名称")
    private String saleOneCateName;
    @ApiModelProperty(value = "销售榜榜一")
    private List<ARPCISaleVO> saleOneList;
    @ApiModelProperty(value = "销售榜榜二分类ID")
    private Long saleTwoCateId;
    @ApiModelProperty(value = "销售榜榜二分类名称")
    private String saleTwoCateName;
    @ApiModelProperty(value = "销售榜榜二")
    private List<ARPCISaleVO> saleTwoList;
    @ApiModelProperty(value = "销售榜榜三分类ID")
    private Long saleThreeCateId;
    @ApiModelProperty(value = "销售榜榜三分类名称")
    private String saleThreeCateName;
    @ApiModelProperty(value = "销售榜榜三")
    private List<ARPCISaleVO> saleThreeList;
    @ApiModelProperty(value = "销售榜榜四分类ID")
    private Long saleFourthCateId;
    @ApiModelProperty(value = "销售榜榜四分类名称")
    private String saleFourthCateName;
    @ApiModelProperty(value = "销售榜榜四 ")
    private List<ARPCISaleVO> saleFourthList;

    @ApiModelProperty(value = "人气榜左轮播图")
    private List<ARPCIPopBannerVO> popLeftList;
    @ApiModelProperty(value = "人气榜中部图")
    private List<ARPCIPopMidVO> popMidList;
    @ApiModelProperty(value = "人气榜右侧图")
    private List<ARPCIPopRightVO> popRightList;

    @ApiModelProperty(value = "首页底部横幅列表")
    private List<ARPCIBottomBannerVO> bottomBannerList;


    @Data
    @ApiModel(value = "顶部横向轮播图")
    public static class ARPCITopLeftBannerVO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口ID")
        private Integer storeId;
        @ApiModelProperty(value = "推广图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "顶部纵向商品")
    public static class ARPCITopRightBannerVO {
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
    public static class ARPCISaleVO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Integer storeId;
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
    public static class ARPCIPopBannerVO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Integer storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "人气榜中部图")
    public static class ARPCIPopMidVO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Integer storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "人气榜右侧图")
    public static class ARPCIPopRightVO {
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
    public static class ARPCIBottomBannerVO {
        @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
        private Integer displayType;
        @ApiModelProperty(value = "档口ID")
        private Integer storeId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String fileUrl;
    }


}
