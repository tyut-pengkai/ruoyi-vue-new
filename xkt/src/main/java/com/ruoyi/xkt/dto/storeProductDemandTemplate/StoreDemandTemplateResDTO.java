package com.ruoyi.xkt.dto.storeProductDemandTemplate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreDemandTemplateResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "尺码30")
    private Integer selectSize30;
    @ApiModelProperty(value = "尺码31")
    private Integer selectSize31;
    @ApiModelProperty(value = "尺码32")
    private Integer selectSize32;
    @ApiModelProperty(value = "尺码33")
    private Integer selectSize33;
    @ApiModelProperty(value = "尺码34")
    private Integer selectSize34;
    @ApiModelProperty(value = "尺码35")
    private Integer selectSize35;
    @ApiModelProperty(value = "尺码36")
    private Integer selectSize36;
    @ApiModelProperty(value = "尺码37")
    private Integer selectSize37;
    @ApiModelProperty(value = "尺码38")
    private Integer selectSize38;
    @ApiModelProperty(value = "尺码39")
    private Integer selectSize39;
    @ApiModelProperty(value = "尺码40")
    private Integer selectSize40;
    @ApiModelProperty(value = "尺码41")
    private Integer selectSize41;
    @ApiModelProperty(value = "尺码42")
    private Integer selectSize42;
    @ApiModelProperty(value = "尺码43")
    private Integer selectSize43;
    @ApiModelProperty(value = "商品信息 工厂名称")
    private Integer selectFacName;
    @ApiModelProperty(value = "商品信息 需求单号")
    private Integer selectDemandCode;
    @ApiModelProperty(value = "商品信息 提单时间")
    private Integer selectMakeTime;
    @ApiModelProperty(value = "商品信息 工厂货号")
    private Integer selectFactoryArtNum;
    @ApiModelProperty(value = "商品信息 商品货号")
    private Integer selectProdArtNum;
    @ApiModelProperty(value = "商品信息 颜色")
    private Integer selectColorName;
    @ApiModelProperty(value = "商品信息 内里材质")
    private Integer selectShoeUpperLiningMaterial;
    @ApiModelProperty(value = "商品信息 鞋面材质")
    private Integer selectShaftMaterial;
    @ApiModelProperty(value = "商品信息 生产状态 1 待生产 2 生产中 3 生产完成")
    private Integer selectDemandStatus;
    @ApiModelProperty(value = "商品信息 紧急程度 0正常 1紧急")
    private Integer selectEmergency;
    @ApiModelProperty(value = "商品信息 总需求数量")
    private Integer selectQuantity;
    @ApiModelProperty(value = "工艺信息 客户名称")
    private Integer selectPartnerName;
    @ApiModelProperty(value = "工艺信息 商标")
    private Integer selectTrademark;
    @ApiModelProperty(value = "工艺信息 鞋型")
    private Integer selectShoeType;
    @ApiModelProperty(value = "工艺信息 楦号")
    private Integer selectShoeSize;
    @ApiModelProperty(value = "工艺信息 主皮")
    private Integer selectMainSkin;
    @ApiModelProperty(value = "工艺信息 主皮用量")
    private Integer selectMainSkinUsage;
    @ApiModelProperty(value = "工艺信息 配皮")
    private Integer selectMatchSkin;
    @ApiModelProperty(value = "工艺信息 配皮用量")
    private Integer selectMatchSkinUsage;
    @ApiModelProperty(value = "工艺信息 领口")
    private Integer selectNeckline;
    @ApiModelProperty(value = "工艺信息 膛底")
    private Integer selectInsole;
    @ApiModelProperty(value = "工艺信息 扣件/拉头")
    private Integer selectFastener;
    @ApiModelProperty(value = "工艺信息 辅料")
    private Integer selectShoeAccessories;
    @ApiModelProperty(value = "工艺信息 包头")
    private Integer selectToeCap;
    @ApiModelProperty(value = "工艺信息 包边")
    private Integer selectEdgeBinding;
    @ApiModelProperty(value = "工艺信息 中大底")
    private Integer selectMidOutsole;
    @ApiModelProperty(value = "工艺信息 防水台")
    private Integer selectPlatformSole;
    @ApiModelProperty(value = "工艺信息 中底厂家编码")
    private Integer selectMidsoleFactoryCode;
    @ApiModelProperty(value = "工艺信息 外底厂家编码")
    private Integer selectOutsoleFactoryCode;
    @ApiModelProperty(value = "工艺信息 跟厂编码")
    private Integer selectHeelFactoryCode;
    @ApiModelProperty(value = "工艺信息 配料")
    private Integer selectComponents;
    @ApiModelProperty(value = "工艺信息 第二底料")
    private Integer selectSecondSoleMaterial;
    @ApiModelProperty(value = "工艺信息 第二配料")
    private Integer selectSecondUpperMaterial;

}
