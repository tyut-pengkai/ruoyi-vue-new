package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.controller.xkt.vo.storeColor.StoreColorVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreProdCreateVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "商品分类ID", required = true)
    @NotNull(message = "商品分类ID不能为空!")
    private Long prodCateId;
    @ApiModelProperty(value = "商品分类名称", required = true)
    @NotBlank(message = "商品分类名称不能为空!")
    private String prodCateName;
    @ApiModelProperty(value = "工厂货号")
    @Size(min = 0, max = 15, message = "工厂货号不能超过60个字!")
    private String factoryArtNum;
    @ApiModelProperty(value = "商品货号", required = true)
    @Size(min = 0, max = 15, message = "商品货号不能超过60个字!")
    @NotBlank(message = "商品货号不能为空!")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题", required = true)
    @Size(min = 0, max = 60, message = "商品标题不能超过60个字!")
    @NotBlank(message = "商品标题不能为空!")
    private String prodTitle;
    @ApiModelProperty(value = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(value = "生产价格")
    private BigDecimal producePrice;
    @ApiModelProperty(value = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(value = "上架方式:1 立即上架 2 定时上架", required = true)
    @NotNull(message = "上架方式不能为空!")
    private Integer listingWay;
    @ApiModelProperty(value = "商品状态：1.未发布 2. 在售 3. 尾货 4.已下架 5. 已删除", required = true)
    @NotNull(message = "商品状态不能为空!")
    private Integer prodStatus;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表", required = true)
    @NotNull(message = "档口文件不能为空!")
    @Valid
    private List<StoreProdFileVO> fileList;
    @NotNull(message = "档口类目属性不能为空!")
    @Valid
    @ApiModelProperty(value = "档口类目属性", required = true)
    private StoreProdCateAttrVO cateAttr;
    @NotNull(message = "档口所有颜色列表不能为空!")
    @ApiModelProperty(value = "档口所有颜色列表", required = true)
    @Valid
    private List<StoreColorVO> allColorList;
    @NotNull(message = "档口尺码列表不能为空!")
    @Valid
    @ApiModelProperty(value = "档口尺码列表", required = true)
    private List<SPCSizeVO> sizeList;
    @Valid
    @NotNull(message = "商品颜色价格列表不能为空!")
    @ApiModelProperty(value = "档口颜色价格列表", required = true)
    private List<SPCColorPriceVO> colorPriceList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcVO svc;
    @NotBlank(message = "详情内容不能为空!")
    @ApiModelProperty(value = "详情内容", required = true)
    private String detail;
    @ApiModelProperty(value = "档口生产工艺")
    private StoreProdProcessVO process;

    @Data
    @Valid
    @ApiModel
    public static class SPCColorPriceVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "档口商品定价不能为空!")
        @ApiModelProperty(value = "档口商品定价", required = true)
        private BigDecimal price;
        @NotNull(message = "排序不能为空!")
        @ApiModelProperty(value = "排序", required = true)
        private Integer orderNum;
    }


    @Data
    @Valid
    @ApiModel
    public static class StoreProdFileVO {
        @NotBlank(message = "文件名称不能为空!")
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @NotBlank(message = "文件路径不能为空!")
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @NotNull(message = "文件大小不能为空!")
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
        @NotNull(message = "文件类型不能为空!")
        @ApiModelProperty(value = "文件类型", required = true)
        private Integer fileType;
        @ApiModelProperty(value = "排序", required = true)
        @NotNull(message = "排序不能为空!")
        private Integer orderNum;
    }

    @Data
    @ApiModel
    public static class StoreProdCateAttrVO {
        @NotBlank(message = "帮面材质不可为空!")
        @ApiModelProperty(value = "帮面材质", required = true)
        private String upperMaterial;
        @NotBlank(message = "鞋垫材质不可为空!")
        @ApiModelProperty(value = "鞋垫材质", required = true)
        private String insoleMaterial;
        @ApiModelProperty(value = "上市季节年份")
        private String releaseYearSeason;
        @ApiModelProperty(value = "靴筒内里材质")
        private String shaftLiningMaterial;
        @ApiModelProperty(value = "靴筒面材质")
        private String shaftMaterial;
        @ApiModelProperty(value = "靴款品名")
        private String shoeStyleName;
        @ApiModelProperty(value = "筒高")
        private String shaftHeight;
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    @ApiModel
    public static class SPCSizeVO {

        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotBlank(message = "内里材质不可为空!")
        @ApiModelProperty(value = "内里材质", required = true)
        private String shoeUpperLiningMaterial;
        @ApiModelProperty(value = "商品尺码", required = true)
        @NotNull(message = "档口商品定价不能为空!")
        private Integer size;
        @NotNull(message = "档口商品定价不能为空!")
        @ApiModelProperty(value = "档口商品定价", required = true)
        private BigDecimal price;
        @NotNull(message = "是否是标准尺码不能为空!")
        @ApiModelProperty(value = "是否是标准尺码", required = true)
        private Integer standard;

    }

    @Data
    @ApiModel
    public static class StoreProdSvcVO {
        @ApiModelProperty(value = "大小码及定制款可退")
        private String customRefund;
        @ApiModelProperty(value = "30天包退")
        private String thirtyDayRefund;
        @ApiModelProperty(value = "一件起批")
        private String oneBatchSale;
        @ApiModelProperty(value = "退款72小时到账")
        private String refundWithinThreeDay;
    }

    @Data
    @ApiModel
    public static class StoreProdProcessVO {
        @ApiModelProperty(value = "客户")
        private String partnerName;
        @ApiModelProperty(value = "商标")
        private String trademark;
        @ApiModelProperty(value = "鞋型")
        private String shoeType;
        @ApiModelProperty(value = "楦号")
        private String shoeSize;
        @ApiModelProperty(value = "主皮")
        private String mainSkin;
        @ApiModelProperty(value = "主皮用量")
        private String mainSkinUsage;
        @ApiModelProperty(value = "配皮")
        private String matchSkin;
        @ApiModelProperty(value = "配皮用量")
        private String matchSkinUsage;
        @ApiModelProperty(value = "领口")
        private String neckline;
        @ApiModelProperty(value = "膛底")
        private String insole;
        @ApiModelProperty(value = "扣件/拉头")
        private String fastener;
        @ApiModelProperty(value = "辅料")
        private String shoeAccessories;
        @ApiModelProperty(value = "包头")
        private String toeCap;
        @ApiModelProperty(value = "包边")
        private String edgeBinding;
        @ApiModelProperty(value = "中大底")
        private String midOutsole;
        @ApiModelProperty(value = "防水台")
        private String platformSole;
        @ApiModelProperty(value = "中底厂家编码")
        private String midsoleFactoryCode;
        @ApiModelProperty(value = "外底厂家编码")
        private String outsoleFactoryCode;
        @ApiModelProperty(value = "跟厂编码")
        private String heelFactoryCode;
        @ApiModelProperty(value = "配料")
        private String components;
        @ApiModelProperty(value = "第二底料")
        private String secondSoleMaterial;
        @ApiModelProperty(value = "第二配料")
        private String secondUpperMaterial;
        @ApiModelProperty(value = "自定义")
        private String customAttr;
    }

}
