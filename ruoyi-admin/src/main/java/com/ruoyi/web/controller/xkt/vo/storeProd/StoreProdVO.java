package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.web.controller.xkt.vo.storeColor.StoreColorVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("档口商品")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdVO {

    @ApiModelProperty("档口商品名称")
    private String prodName;
    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "商品分类ID")
    @NotNull(message = "商品分类ID不能为空!")
    private Long prodCateId;
    @ApiModelProperty(name = "工厂货号")
    @Size(max = 15, message = "工厂货号不能超过60个字!")
    private String factoryArtNum;
    @ApiModelProperty(name = "商品货号")
    @Size(max = 15, message = "商品货号不能超过60个字!")
    @NotBlank(message = "商品货号不能为空!")
    private String prodArtNum;
    @ApiModelProperty(name = "商品标题")
    @Size(max = 60, message = "商品标题不能超过60个字!")
    @NotBlank(message = "商品标题不能为空!")
    private String prodTitle;
    @ApiModelProperty(name = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(name = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(name = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(name = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(name = "上架方式")
    @NotBlank(message = "上架方式不能为空!")
    private Integer listingWay;
    @ApiModelProperty(name = "商品状态")
    @NotBlank(message = "商品状态不能为空!")
    private Integer prodStatus;
    @ApiModelProperty(name = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date listingWaySchedule;
    @ApiModelProperty(name = "档口文件列表")
    @NotNull(message = "档口文件不能为空!")
    @Valid
    private List<StoreProdFileVO> fileList;
    @NotNull(message = "档口类目属性列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @NotNull(message = "档口所有颜色列表不能为空!")
    @ApiModelProperty(name = "档口所有颜色列表")
    private List<StoreColorVO> allColorList;
    @NotNull(message = "商品颜色列表不能为空!")
    @Valid
    @ApiModelProperty(name = "商品颜色列表")
    private List<StoreProdColorVO> colorList;
    @NotNull(message = "档口尺码列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口尺码列表")
    private List<StoreProdColorSizeVO> sizeList;
    @NotNull(message = "档口颜色价格列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口颜色价格列表")
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(name = "档口服务承诺")
    private StoreProdSvcVO svc;
    @Valid
    @ApiModelProperty(name = "详情内容")
    private StoreProdDetailVO detail;
    @ApiModelProperty(name = "档口生产工艺")
    private StoreProdProcessVO process;

    @Data
    public static class StoreProdFileVO {
        @NotBlank(message = "文件名称不能为空!")
        @ApiModelProperty(name = "文件名称")
        private String fileName;
        @NotBlank(message = "文件路径不能为空!")
        @ApiModelProperty(name = "文件路径")
        private String fileUrl;
        @NotNull(message = "文件大小不能为空!")
        @ApiModelProperty(name = "文件大小")
        private BigDecimal fileSize;
        @NotBlank(message = "文件类型不能为空!")
        @ApiModelProperty(name = "文件类型")
        private Integer fileType;
        @ApiModelProperty(name = "排序")
        @NotNull(message = "排序不能为空!")
        private Integer orderNum;
    }

    @Data
    public static class StoreProdCateAttrVO {
        @ApiModelProperty(name = "系统设置类目")
        private String dictType;
        @ApiModelProperty(name = "系统设置类目值")
        private String dictValue;
    }

    @Data
    public static class StoreProdColorVO {
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(name = "档口颜色ID")
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(name = "颜色名称")
        private String colorName;
        @NotNull(message = "排序不能为空!")
        @ApiModelProperty(name = "排序")
        private Integer orderNum;
    }

    @Data
    public static class StoreProdColorSizeVO {
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(name = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(name = "商品尺码")
        @NotNull(message = "档口商品定价不能为空!")
        private Integer size;
        @NotBlank(message = "是否是标准尺码不能为空!")
        @ApiModelProperty(name = "是否是标准尺码")
        private Integer standard;
    }

    @Data
    public static class StoreProdColorPriceVO {
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(name = "档口商品颜色ID")
        private Long storeColorId;
        @NotNull(message = "档口商品定价不能为空!")
        @ApiModelProperty(name = "档口商品定价")
        private BigDecimal price;
    }

    @Data
    public static class StoreProdSvcVO {
        @ApiModelProperty(name = "大小码及定制款可退")
        private String customRefund;
        @ApiModelProperty(name = "30天包退")
        private String thirtyDayRefund;
        @ApiModelProperty(name = "一件起批")
        private String oneBatchSale;
        @ApiModelProperty(name = "退款72小时到账")
        private String refundWithinThreeDay;
    }

    @Data
    public static class StoreProdDetailVO {
        @NotBlank(message = "详情内容不能为空!")
        @ApiModelProperty(name = "详情内容")
        private String detail;
    }

    @Data
    public static class StoreProdProcessVO {
        @ApiModelProperty(name = "鞋型")
        private String shoeType;
        @ApiModelProperty(name = "楦号")
        private String shoeSize;
        @ApiModelProperty(name = "主皮")
        private String mainSkin;
        @ApiModelProperty(name = "主皮用量")
        private String mainSkinUsage;
        @ApiModelProperty(name = "配皮")
        private String matchSkin;
        @ApiModelProperty(name = "配皮用量")
        private String matchSkinUsage;
        @ApiModelProperty(name = "领口")
        private String neckline;
        @ApiModelProperty(name = "膛底")
        private String insole;
        @ApiModelProperty(name = "扣件/拉头")
        private String fastener;
        @ApiModelProperty(name = "辅料")
        private String shoeAccessories;
        @ApiModelProperty(name = "包头")
        private String toeCap;
        @ApiModelProperty(name = "包边")
        private String edgeBinding;
        @ApiModelProperty(name = "中大底")
        private String midOutsole;
        @ApiModelProperty(name = "防水台")
        private String platformSole;
        @ApiModelProperty(name = "中底厂家编码")
        private String midsoleFactoryCode;
        @ApiModelProperty(name = "外底厂家编码")
        private String outsoleFactoryCode;
        @ApiModelProperty(name = "跟厂编码")
        private String heelFactoryCode;
        @ApiModelProperty(name = "配料")
        private String components;
        @ApiModelProperty(name = "第二底料")
        private String secondSoleMaterial;
        @ApiModelProperty(name = "第二配料")
        private String secondUpperMaterial;
        @ApiModelProperty(name = "自定义")
        private String customAttr;
    }

}
