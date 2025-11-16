package com.ruoyi.web.controller.xkt.vo.storeProdProcess;

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
public class StoreProdProcessResVO {

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
