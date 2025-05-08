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
@ApiModel("档口商品APP详情")
@Data
public class StoreProdAppResVO {

    @ApiModelProperty(value = "档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "是否已收藏商品")
    private Boolean collectProd;
    @ApiModelProperty(value = "最低的商品价格")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "规格")
    private String specification;
    @ApiModelProperty(value = "商品标签列表")
    private List<String> tagList;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFileVO> fileList;
    @ApiModelProperty(value = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcVO svc;
    @ApiModelProperty(value = "详情内容")
    private StoreProdDetailVO detail;

    @Data
    @ApiModel(value = "档口商品文件")
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
    @ApiModel(value = "档口商品类目属性")
    public static class StoreProdCateAttrVO {
        @ApiModelProperty(value = "系统设置类目")
        private String dictType;
        @ApiModelProperty(value = "系统设置类目值")
        private String dictValue;
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

}
