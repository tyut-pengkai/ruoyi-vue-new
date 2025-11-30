package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
public class StoreOrderPrintReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long storeOrderId;

    @NotEmpty(message = "订单明细ID不能为空")
    @ApiModelProperty(value = "订单明细ID", required = true)
    private List<Long> storeOrderDetailIds;

    @NotNull(message = "物流ID不能为空")
    @ApiModelProperty(value = "物流ID", required = true)
    private Long expressId;

    @ApiModelProperty(value = "打印成功后立即发货", required = true)
    private Boolean needShip;

    @NotEmpty(message = "发货人名称不能为空")
    @ApiModelProperty(value = "发货人-名称", required = true)
    private String originContactName;

    @NotEmpty(message = "发货人电话不能为空")
    @ApiModelProperty(value = "发货人-电话", required = true)
    private String originContactPhoneNumber;

    @NotEmpty(message = "发货人省编码不能为空")
    @ApiModelProperty(value = "发货人-省编码", required = true)
    private String originProvinceCode;

    @NotEmpty(message = "发货人市编码不能为空")
    @ApiModelProperty(value = "发货人-市编码", required = true)
    private String originCityCode;

    @NotEmpty(message = "发货人区县编码不能为空")
    @ApiModelProperty(value = "发货人-区县编码", required = true)
    private String originCountyCode;

    @NotEmpty(message = "发货人详细地址不能为空")
    @ApiModelProperty(value = "发货人-详细地址", required = true)
    private String originDetailAddress;

    @NotEmpty(message = "收货人名称不能为空")
    @ApiModelProperty(value = "收货人-名称", required = true)
    private String destinationContactName;

    @NotEmpty(message = "收货人电话不能为空")
    @ApiModelProperty(value = "收货人-电话", required = true)
    private String destinationContactPhoneNumber;

    @NotEmpty(message = "收货人省编码不能为空")
    @ApiModelProperty(value = "收货人-省编码", required = true)
    private String destinationProvinceCode;

    @NotEmpty(message = "收货人市编码不能为空")
    @ApiModelProperty(value = "收货人-市编码", required = true)
    private String destinationCityCode;

    @NotEmpty(message = "收货人区县编码不能为空")
    @ApiModelProperty(value = "收货人-区县编码", required = true)
    private String destinationCountyCode;

    @NotEmpty(message = "收货人详细地址不能为空")
    @ApiModelProperty(value = "收货人-详细地址", required = true)
    private String destinationDetailAddress;
}
