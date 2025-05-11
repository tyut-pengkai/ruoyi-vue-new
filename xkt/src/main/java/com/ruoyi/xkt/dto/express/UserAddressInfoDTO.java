package com.ruoyi.xkt.dto.express;

import com.ruoyi.xkt.domain.UserAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-05-11 22:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAddressInfoDTO extends UserAddress {
    /**
     * 省
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区县
     */
    private String districtName;

    /**
     * 区县
     */
    private String countyCode;

    /**
     * 区县
     */
    private String countyName;

    /**
     * 完整地址，新增/修改接口使用
     */
    private String address;
}
