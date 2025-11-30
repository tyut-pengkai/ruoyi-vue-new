package com.ruoyi.xkt.dto.order;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-11-30
 */
@Data
public class ShipAddressDTO {
    /**
     * 发货人-名称
     */
    private String originContactName;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 发货人-省编码
     */
    private String originProvinceCode;
    /**
     * 发货人-市编码
     */
    private String originCityCode;
    /**
     * 发货人-区县编码
     */
    private String originCountyCode;
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
}
