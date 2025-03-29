package com.ruoyi.web.controller.xkt.vo.storeProdProcess;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品工艺信息")
@Data
public class StoreProdProcessVO {

    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
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
