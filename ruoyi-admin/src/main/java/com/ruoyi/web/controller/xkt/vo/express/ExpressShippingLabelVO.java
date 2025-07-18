package com.ruoyi.web.controller.xkt.vo.express;

import com.ruoyi.common.utils.desensitization.Desensitization;
import com.ruoyi.common.utils.desensitization.SensitiveTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 快递面单
 *
 * @author liangyq
 * @date 2025-07-18
 **/
@ApiModel
@Data
public class ExpressShippingLabelVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 运单号
     */
    @ApiModelProperty(value = "运单号")
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型")
    private String vasType;
    /**
     * 转运代码
     */
    @ApiModelProperty(value = "转运代码")
    private String mark;
    /**
     * 短转运代码
     */
    @ApiModelProperty(value = "短转运代码")
    private String shortMark;
    /**
     * 集包地
     */
    @ApiModelProperty(value = "集包地")
    private String bagAddr;
    /**
     * 最后打印时间
     */
    @ApiModelProperty(value = "最后打印时间")
    private Date lastPrintTime;
    /**
     * 打印次数
     */
    @ApiModelProperty(value = "打印次数")
    private Integer printCount;
    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private String goodsInfo;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 发货人-名称
     */
    @ApiModelProperty(value = "发货人-名称")
    private String originContactName;
    /**
     * 发货人-电话
     */
    @Desensitization(SensitiveTypeEnum.PHONE_NO)
    @ApiModelProperty(value = "发货人-电话（已脱敏）")
    private String originContactPhoneNumber;
    /**
     * 发货人-省
     */
    @ApiModelProperty(value = "发货人-省")
    private String originProvinceName;
    /**
     * 发货人-市
     */
    @ApiModelProperty(value = "发货人-市")
    private String originCityName;
    /**
     * 发货人-区县
     */
    @ApiModelProperty(value = "发货人-区县")
    private String originCountyName;
    /**
     * 发货人-详细地址
     */
    @ApiModelProperty(value = "发货人-详细地址")
    private String originDetailAddress;
    /**
     * 收货人-名称
     */
    @ApiModelProperty(value = "收货人-名称")
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    @Desensitization(SensitiveTypeEnum.PHONE_NO)
    @ApiModelProperty(value = "收货人-电话（已脱敏）")
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省
     */
    @ApiModelProperty(value = "收货人-省")
    private String destinationProvinceName;
    /**
     * 收货人-市
     */
    @ApiModelProperty(value = "收货人-市")
    private String destinationCityName;
    /**
     * 收货人-区县
     */
    @ApiModelProperty(value = "收货人-区县")
    private String destinationCountyName;
    /**
     * 收货人-详细地址
     */
    @ApiModelProperty(value = "收货人-详细地址")
    private String destinationDetailAddress;
}
