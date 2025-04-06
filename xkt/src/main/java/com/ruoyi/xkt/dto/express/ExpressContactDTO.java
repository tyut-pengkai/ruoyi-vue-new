package com.ruoyi.xkt.dto.express;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-06 17:35
 */
@Data
public class ExpressContactDTO {
    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 区县编码
     */
    private String countyCode;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系电话
     */
    private String contactPhoneNumber;
}
