package com.ruoyi.xkt.dto.storeHomepage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdStatusCountDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHomeProdResDTO {

    @ApiModelProperty(value = "档口基本信息")
    private StoreBasicDTO store;
    @ApiModelProperty(value = "档口商品不同状态数量")
    private StoreProdStatusCountDTO storeProdStatusCount;
    @ApiModelProperty(value = "档口商品基本信息")
    private StoreProdInfoDTO storeProd;
    @ApiModelProperty(value = "是否已关注档口")
    private Integer followStore;
    @ApiModelProperty(value = "是否已收藏商品")
    private Integer collectProd;
    @ApiModelProperty(value = "推荐商品列表")
    private List<DecorationVO> recommendedList;

    @Data
    @Accessors(chain = true)
    public static class DecorationVO {
        @ApiModelProperty(value = "业务类型ID，如果选择：不跳转 不传，选择：跳转店铺，传storeId，选择：跳转商品，传storeProdId")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "跳转类型 1. 不跳转 2. 跳转店铺 3. 跳转商品")
        private Integer jumpType;
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型 1轮播大图")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

    @Data
    @ApiModel(value = "档口基本信息")
    @Accessors(chain = true)
    public static class StoreBasicDTO {
        @ApiModelProperty(value = "档口模板ID")
        private Integer templateNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "经营年限")
        private Integer operateYears;
        @ApiModelProperty(value = "联系电话")
        private String contactPhone;
        @ApiModelProperty(value = "备选联系电话")
        private String contactBackPhone;
        @ApiModelProperty(value = "微信账号")
        private String wechatAccount;
        @ApiModelProperty(value = "QQ账号")
        private String qqAccount;
        @ApiModelProperty(value = "档口地址")
        private String storeAddress;
        @ApiModelProperty(value = "营业执照名称")
        private String licenseName;
    }

    @Data
    @ApiModel(value = "档口商品基本信息")
    @Accessors(chain = true)
    public static class StoreProdInfoDTO {
        @ApiModelProperty("档口商品ID")
        private Long storeProdId;
        @ApiModelProperty("档口商品名称")
        private String prodName;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "创建时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createTime;
        @ApiModelProperty(value = "商品标题")
        private String prodTitle;
        @ApiModelProperty(value = "大小码加价")
        private Integer overPrice;
        @ApiModelProperty(value = "详情内容")
        private StoreProdDetailDTO detail;
        @ApiModelProperty(value = "档口商品尺码库存列表")
        private List<StoreProdColorDTO> colorList;
        @ApiModelProperty(value = "档口文件列表")
        private List<StoreProdFileResDTO> mainPicList;
        @ApiModelProperty(value = "档口类目属性列表")
        private List<StoreProdCateAttrDTO> cateAttrList;
    }

    @Data
    @ApiModel(value = "档口商品基本信息")
    @Accessors(chain = true)
    public static class StoreProdColorDTO {
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
        @ApiModelProperty(value = "商品尺码及库存")
        List<StoreProdSizeStockDTO> sizeStockList;
    }

    @Data
    @ApiModel(value = "档口商品尺码及库存")
    @Accessors(chain = true)
    public static class StoreProdSizeStockDTO {
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

}
