package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品PC详情")
@Data
@Accessors(chain = true)
public class StoreProdPCResDTO {

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
    private List<StoreProdSkuItemDTO> colorList;
    @ApiModelProperty(value = "档口类目属性")
    private StoreProdCateAttrDTO cateAttr;
    @ApiModelProperty(value = "详情内容")
    private String detail;
    @ApiModelProperty(value = "商品主图视频及主图")
    private List<StoreProdFileResDTO> fileList;

    @Data
    public static class StoreProdCateAttrDTO {
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
