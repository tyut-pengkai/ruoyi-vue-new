package com.ruoyi.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WechatpayConfig {
    private String notifyUrl;
    private String appId;
    private String mchId;
    private String partnerKey;
}
