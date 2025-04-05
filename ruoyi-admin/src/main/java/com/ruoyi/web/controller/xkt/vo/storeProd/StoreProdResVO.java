package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.controller.xkt.vo.storeColor.StoreColorVO;
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
@ApiModel("档口商品")
@Data
public class StoreProdResVO {

    @ApiModelProperty(value = "档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "工厂货号")
    private String factoryArtNum;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(value = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(value = "上架方式")
    private Integer listingWay;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFileVO> fileList;
    @ApiModelProperty(value = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @ApiModelProperty(value = "档口所有颜色列表")
    private List<StoreColorVO> allColorList;
    @ApiModelProperty(value = "商品颜色列表")
    private List<StoreColorVO> colorList;
    @ApiModelProperty(value = "档口颜色价格列表")
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcVO svc;
    @ApiModelProperty(value = "详情内容")
    private StoreProdDetailVO detail;
    @ApiModelProperty(value = "档口商品生产工艺")
    private StoreProdProcessVO process;


    @Data
    @ApiModel(value = "档口商品文件")
    public static class StoreProdFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

    @Data
    @ApiModel(value = "档口商品类目属性")
    public static class StoreProdCateAttrVO {
        @ApiModelProperty(value = "系统设置类目")
        private String dictType;
        @ApiModelProperty(value = "系统设置类目值")
        private String dictValue;
    }

    @Data
    @ApiModel(value = "档口商品颜色尺码")
    public static class StoreProdColorSizeVO {
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "是否是标准尺码")
        private Integer standard;
    }

    @Data
    @ApiModel(value = "档口商品颜色价格")
    public static class StoreProdColorPriceVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
    }

    @Data
    @ApiModel(value = "档口商品服务")
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
    @ApiModel(value = "档口商品详情")
    public static class StoreProdDetailVO {
        @ApiModelProperty(value = "详情内容")
        private String detail;
    }

    @Data
    @ApiModel(value = "档口商品工艺")
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
