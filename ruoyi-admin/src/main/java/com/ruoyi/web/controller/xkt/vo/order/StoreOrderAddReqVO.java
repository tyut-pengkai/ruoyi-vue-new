package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:31
 */
@ApiModel("订单新增参数")
@Data
public class StoreOrderAddReqVO {
    /**
     * 档口ID
     */
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderRemark;
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 收货人-名称
     */
    @ApiModelProperty(value = "收货人-名称")
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    @ApiModelProperty(value = "收货人-电话")
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    @ApiModelProperty(value = "收货人-省编码")
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    @ApiModelProperty(value = "收货人-市编码")
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    @ApiModelProperty(value = "收货人-区县编码")
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    @ApiModelProperty(value = "收货人-详细地址")
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    @ApiModelProperty(value = "发货方式[1:货其再发 2:有货先发]")
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    @ApiModelProperty(value = "最晚发货时间")
    private Date deliveryEndTime;
    /**
     * 明细列表
     */
    @ApiModelProperty(value = "明细列表")
    private List<Detail> detailList;

    @ApiModelProperty(value = "支付渠道[1:支付宝]")
    private Integer payChannel;

    @ApiModelProperty(value = "支付来源[1:电脑网站 2:手机网站]")
    private Integer payFrom;

    @ApiModel(value = "明细")
    @Data
    public static class Detail {

        @ApiModelProperty(value = "商品颜色尺码ID")
        private Long storeProdColorSizeId;

        @ApiModelProperty(value = "商品数量")
        private Integer goodsQuantity;
    }
}
