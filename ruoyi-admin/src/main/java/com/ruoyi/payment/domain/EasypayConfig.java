package com.ruoyi.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EasypayConfig {
    private String serverUrl;
    private String notifyUrl;
    private String returnUrl;
    private String pid;
    private String key;
}
