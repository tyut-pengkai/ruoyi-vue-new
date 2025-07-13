package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品PC详情")
@Data
public class StoreProdPCResVO {

    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "是否已收藏商品")
    private Boolean collectProd;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "颜色列表")
    private List<SPPCColorVO> colorList;
    @ApiModelProperty(value = "档口类目属性")
    private StoreProdCateAttrVO cateAttr;
    @ApiModelProperty(value = "详情内容")
    private String detail;
    @ApiModelProperty(value = "商品主图视频及主图")
    private List<StoreProdFileVO> fileList;

    @Data
    @ApiModel
    public static class SPPCColorVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
        @ApiModelProperty(value = "尺码库存列表")
        List<SPPCSizeStockVO> sizeStockList;
    }

    @Data
    @ApiModel
    public static class SPPCSizeStockVO {
        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorSizeId;
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "是否是标准尺码")
        private Integer standard;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

    @Data
    @ApiModel
    public static class StoreProdFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型 1商品主图  2商品主图视频 3下载图片包")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

    @Data
    @ApiModel
    public static class StoreProdCateAttrVO {
        @ApiModelProperty(value = "帮面材质")
        private String upperMaterial;
        @ApiModelProperty(value = "内里材质")
        private String liningMaterial;
        @ApiModelProperty(value = "鞋垫材质")
        private String insoleMaterial;
        @ApiModelProperty(value = "上市季节年份")
        private String releaseYearSeason;
        @ApiModelProperty(value = "后跟高")
        private String heelHeight;
        @ApiModelProperty(value = "跟底款式")
        private String heelType;
        @ApiModelProperty(value = "鞋头款式")
        private String toeStyle;
        @ApiModelProperty(value = "适合季节")
        private String suitableSeason;
        @ApiModelProperty(value = "开口深度")
        private String collarDepth;
        @ApiModelProperty(value = "鞋底材质")
        private String outsoleMaterial;
        @ApiModelProperty(value = "风格")
        private String style;
        @ApiModelProperty(value = "款式")
        private String design;
        @ApiModelProperty(value = "皮质特征")
        private String leatherFeatures;
        @ApiModelProperty(value = "制作工艺")
        private String manufacturingProcess;
        @ApiModelProperty(value = "图案")
        private String pattern;
        @ApiModelProperty(value = "闭合方式")
        private String closureType;
        @ApiModelProperty(value = "适用场景")
        private String occasion;
        @ApiModelProperty(value = "适用年龄")
        private String suitableAge;
        @ApiModelProperty(value = "厚薄")
        private String thickness;
        @ApiModelProperty(value = "流行元素")
        private String fashionElements;
        @ApiModelProperty(value = "适用对象")
        private String suitablePerson;
    }

}
