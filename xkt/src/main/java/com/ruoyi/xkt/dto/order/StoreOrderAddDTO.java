package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:31
 */
@Data
public class StoreOrderAddDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 下单用户ID
     */
    private Long orderUserId;
    /**
     * 订单备注
     */
    private String orderRemark;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 收货人-名称
     */
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    private String destinationProvinceCode;
    /**
     * 收货人-市编码
     */
    private String destinationCityCode;
    /**
     * 收货人-区县编码
     */
    private String destinationCountyCode;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;
    /**
     * 发货方式[1:货其再发 2:有货先发]
     */
    private Integer deliveryType;
    /**
     * 最晚发货时间
     */
    private Date deliveryEndTime;
    /**
     * 明细列表
     */
    private List<StoreOrderDetailAddDTO> detailList;

}
