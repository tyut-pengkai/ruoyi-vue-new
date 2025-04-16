package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-16 16:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpressStructAddressDTO {
    /**
     * 名称
     */
    private String contactName;
    /**
     * 电话
     */
    private String contactPhoneNumber;
    /**
     * 省
     */
    private String provinceCode;
    private String provinceName;
    /**
     * 市
     */
    private String cityCode;
    private String cityName;
    /**
     * 区县
     */
    private String countyCode;
    private String countyName;
    /**
     * 详细地址
     */
    private String detailAddress;
}
