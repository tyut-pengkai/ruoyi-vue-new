package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-16 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressShipReqDTO {
    /**
     * 请求号
     */
    private String expressReqNo;
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
    private String originProvinceName;
    /**
     * 发货人-市编码
     */
    private String originCityCode;
    private String originCityName;
    /**
     * 发货人-区县编码
     */
    private String originCountyCode;
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
     * 收货人-省编码
     */
    private String destinationProvinceCode;
    private String destinationProvinceName;
    /**
     * 收货人-市编码
     */
    private String destinationCityCode;
    private String destinationCityName;
    /**
     * 收货人-区县编码
     */
    private String destinationCountyCode;
    private String destinationCountyName;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;

}
