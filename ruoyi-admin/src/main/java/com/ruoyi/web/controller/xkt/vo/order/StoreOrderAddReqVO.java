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
@ApiModel
@Data
public class StoreOrderAddReqVO {
    /**
     * 档口ID
     */
    @ApiModelProperty("档口ID")
    private Long storeId;
//    /**
//     * 下单用户ID
//     */
//    private Long orderUserId;
    /**
     * 订单备注
     */
    @ApiModelProperty("订单备注")
    private String orderRemark;
    /**
     * 物流ID
     */
    @ApiModelProperty("物流ID")
    private Long expressId;
    /**
     * 收货人-名称
     */
    @ApiModelProperty("收货人-名称")
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    @ApiModelProperty("收货人-电话")
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    @ApiModelProperty("收货人-省编码")
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    @ApiModelProperty("收货人-市编码")
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    @ApiModelProperty("收货人-区县编码")
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    @ApiModelProperty("收货人-详细地址")
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    @ApiModelProperty("发货方式[1:货其再发 2:有货先发]")
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    @ApiModelProperty("最晚发货时间")
    private Date deliveryEndTime;
    /**
     * 明细列表
     */
    @ApiModelProperty("明细列表")
    private List<Detail> detailList;

    @ApiModel
    @Data
    public static class Detail {

        @ApiModelProperty("商品颜色尺码ID")
        private Long storeProdColorSizeId;

        @ApiModelProperty("商品数量")
        private Integer goodsQuantity;
    }
}
