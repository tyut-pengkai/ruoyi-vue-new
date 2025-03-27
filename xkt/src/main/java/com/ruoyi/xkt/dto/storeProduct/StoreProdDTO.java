package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("创建档口商品")
@Data
public class StoreProdDTO {

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
    private Long listingWay;
    @ApiModelProperty(name = "下一个生成的条形码尾号")
    private Integer nextBarcodeNum;
    @ApiModelProperty(name = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date listingWaySchedule;

}
