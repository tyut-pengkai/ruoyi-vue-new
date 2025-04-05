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

    @ApiModelProperty("档口商品名称")
    private Long storeProdId;
    @ApiModelProperty("档口ID")
    private Long storeId;
    @ApiModelProperty("档口商品名称")
    private String prodName;
    @ApiModelProperty(name = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(name = "工厂货号")
    private String factoryArtNum;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "商品标题")
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
    private Integer listingWay;
    @ApiModelProperty(name = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date listingWaySchedule;
    @ApiModelProperty(name = "档口文件列表")
    private List<StoreProdFileVO> fileList;
    @ApiModelProperty(name = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @ApiModelProperty(name = "档口所有颜色列表")
    private List<StoreColorVO> allColorList;
    @ApiModelProperty(name = "商品颜色列表")
    private List<StoreColorVO> colorList;
    @ApiModelProperty(name = "档口颜色价格列表")
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(name = "档口服务承诺")
    private StoreProdSvcVO svc;
    @ApiModelProperty(name = "详情内容")
    private StoreProdDetailVO detail;
    @ApiModelProperty(name = "档口商品生产工艺")
    private StoreProdProcessVO process;


    @Data
    public static class StoreProdFileVO {
        @ApiModelProperty(name = "文件名称")
        private String fileName;
        @ApiModelProperty(name = "文件路径")
        private String fileUrl;
        @ApiModelProperty(name = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(name = "文件类型")
        private Integer fileType;
        @ApiModelProperty(name = "排序")
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
    public static class StoreProdColorSizeVO {
        @ApiModelProperty(name = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(name = "商品尺码")
        private Integer size;
        @ApiModelProperty(name = "是否是标准尺码")
        private Integer standard;
    }

    @Data
    public static class StoreProdColorPriceVO {
        @ApiModelProperty(name = "档口商品颜色ID")
        private Long storeColorId;
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
