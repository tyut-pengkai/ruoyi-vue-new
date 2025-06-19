package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:31
 */
@ApiModel("订单修改参数")
@Data
public class StoreOrderUpdateReqVO {
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long id;
    /**
     * 档口ID
     */
    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderRemark;
    /**
     * 物流ID
     */
    @NotNull(message = "物流ID不能为空")
    @ApiModelProperty(value = "物流ID", required = true)
    private Long expressId;
    /**
     * 收货人-名称
     */
    @NotEmpty(message = "收货人名称不能为空")
    @ApiModelProperty(value = "收货人-名称", required = true)
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    @NotEmpty(message = "收货人电话不能为空")
    @ApiModelProperty(value = "收货人-电话", required = true)
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    @NotEmpty(message = "收货人省编码不能为空")
    @ApiModelProperty(value = "收货人-省编码", required = true)
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    @NotEmpty(message = "收货人市编码不能为空")
    @ApiModelProperty(value = "收货人-市编码", required = true)
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    @NotEmpty(message = "收货人区县编码不能为空")
    @ApiModelProperty(value = "收货人-区县编码", required = true)
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    @NotEmpty(message = "收货人详细地址不能为空")
    @ApiModelProperty(value = "收货人-详细地址", required = true)
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    @NotNull(message = "发货方式不能为空")
    @ApiModelProperty(value = "发货方式[1:货其再发 2:有货先发]", required = true)
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    @ApiModelProperty(value = "最晚发货时间")
    private Date deliveryEndTime;
    /**
     * 明细列表
     */
    @Valid
    @NotEmpty(message = "订单明细不能为空")
    @ApiModelProperty(value = "明细列表", required = true)
    private List<Detail> detailList;

    @ApiModel(value = "明细")
    @Data
    public static class Detail {

        @NotNull(message = "商品颜色尺码ID不能为空")
        @ApiModelProperty(value = "商品颜色尺码ID", required = true)
        private Long storeProdColorSizeId;

        @NotNull(message = "商品数量不能为空")
        @ApiModelProperty(value = "商品数量", required = true)
        private Integer goodsQuantity;
    }
}
