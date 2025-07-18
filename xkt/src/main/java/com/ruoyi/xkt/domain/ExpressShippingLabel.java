package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 快递面单
 *
 * @author liangyq
 * @date 2025-07-18
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressShippingLabel extends SimpleEntity {
    /**
     * 运单号
     */
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 服务类型
     */
    private String vasType;
    /**
     * 转运代码
     */
    private String mark;
    /**
     * 短转运代码
     */
    private String shortMark;
    /**
     * 集包地
     */
    private String bagAddr;
    /**
     * 最后打印时间
     */
    private Date lastPrintTime;
    /**
     * 打印次数
     */
    private Integer printCount;
    /**
     * 商品信息
     */
    private String goodsInfo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 发货人-名称
     */
    private String originContactName;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 发货人-省
     */
    private String originProvinceName;
    /**
     * 发货人-市
     */
    private String originCityName;
    /**
     * 发货人-区县
     */
    private String originCountyName;
    /**
     * 发货人-详细地址
     */
    private String originDetailAddress;
    /**
     * 收货人-名称
     */
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省
     */
    private String destinationProvinceName;
    /**
     * 收货人-市
     */
    private String destinationCityName;
    /**
     * 收货人-区县
     */
    private String destinationCountyName;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;
}
