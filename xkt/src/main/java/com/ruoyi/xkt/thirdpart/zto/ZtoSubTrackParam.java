package com.ruoyi.xkt.thirdpart.zto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:11
 */
@Data
public class ZtoSubTrackParam {
    /**
     * 运单号
     */
    private String billCode;
    /**
     * 收寄人任一方电话号码后4位（手机或座机）。通过电话号码鉴权时必填，鉴权方式可以电子面单账号或电话号码二选一。
     * 选择电子面单账号鉴权时，该字段非必填；选择电话号码鉴权时，可以不绑定下单电子面单账号。
     */
    private String mobilePhone;
}
