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

    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "商品分类ID", required = true)
    @NotNull(message = "商品分类ID不能为空!")
    private Long prodCateId;
    @ApiModelProperty(value = "工厂货号")
    @Size(min = 1, max = 15, message = "工厂货号不能超过60个字!")
    private String factoryArtNum;
    @ApiModelProperty(value = "商品货号", required = true)
    @Size(min = 1, max = 15, message = "商品货号不能超过60个字!")
    @NotBlank(message = "商品货号不能为空!")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题", required = true)
    @Size(min = 1, max = 60, message = "商品标题不能超过60个字!")
    @NotBlank(message = "商品标题不能为空!")
    private String prodTitle;
    @ApiModelProperty(value = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(value = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(value = "上架方式:1 立即上架 2 定时上架", required = true)
    @NotNull(message = "上架方式不能为空!")
    private Integer listingWay;
    @ApiModelProperty(value = "商品状态：1.未发布 2. 在售 3. 尾货 4.已下架 4. 已删除", required = true)
    @NotNull(message = "商品状态不能为空!")
    private Integer prodStatus;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表", required = true)
    @NotNull(message = "档口文件不能为空!")
    @Valid
    private List<StoreProdFileVO> fileList;
    @NotNull(message = "档口类目属性列表不能为空!")
    @Valid
    @ApiModelProperty(value = "档口类目属性列表", required = true)
    private List<StoreProdCateAttrVO> cateAttrList;
    @NotNull(message = "档口所有颜色列表不能为空!")
    @ApiModelProperty(value = "档口所有颜色列表", required = true)
    private List<StoreColorVO> allColorList;
    @NotNull(message = "商品颜色列表不能为空!")
    @Valid
    @ApiModelProperty(value = "商品颜色列表", required = true)
    private List<StoreProdColorVO> colorList;
    @NotNull(message = "档口尺码列表不能为空!")
    @Valid
    @ApiModelProperty(value = "档口尺码列表", required = true)
    private List<StoreProdColorSizeVO> sizeList;
    @NotNull(message = "档口颜色价格列表不能为空!")
    @Valid
    @ApiModelProperty(value = "档口颜色价格列表", required = true)
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcVO svc;
    @NotNull(message = "详情内容不能为空!")
    @Valid
    @ApiModelProperty(value = "详情内容", required = true)
    private StoreProdDetailVO detail;
    @ApiModelProperty(value = "档口生产工艺")
    private StoreProdProcessVO process;

    @Data
    @ApiModel(value = "档口文件列表")
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
    @ApiModel(value = "档口类目属性列表")
    public static class StoreProdCateAttrVO {
        @ApiModelProperty(value = "系统设置类目")
        private String dictType;
        @ApiModelProperty(value = "系统设置类目值")
        private String dictValue;
    }

    @Data
    @ApiModel(value = "商品颜色列表")
    public static class StoreProdColorVO {
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "排序不能为空!")
        @ApiModelProperty(value = "排序", required = true)
        private Integer orderNum;
    }

    @Data
    @ApiModel(value = "档口尺码列表")
    public static class StoreProdColorSizeVO {
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @ApiModelProperty(value = "商品尺码", required = true)
        @NotNull(message = "档口商品定价不能为空!")
        private Integer size;
        @NotNull(message = "是否是标准尺码不能为空!")
        @ApiModelProperty(value = "是否是标准尺码", required = true)
        private Integer standard;
    }

    @Data
    @ApiModel(value = "档口颜色价格列表")
    public static class StoreProdColorPriceVO {
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeColorId;
        @NotNull(message = "档口商品定价不能为空!")
        @ApiModelProperty(value = "档口商品定价", required = true)
        private BigDecimal price;
    }

    @Data
    @ApiModel(value = "档口服务承诺")
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
    @ApiModel(value = "详情内容")
    public static class StoreProdDetailVO {
        @NotBlank(message = "详情内容不能为空!")
        @ApiModelProperty(value = "详情内容", required = true)
        private String detail;
    }

    @Data
    @ApiModel(value = "档口生产工艺")
    public static class StoreProdProcessVO {
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
