package com.ruoyi.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlipayConfig {
    private String notifyUrl;
    private String appId;
    private String privateKey;
    private String alipayPublicKey;
}
