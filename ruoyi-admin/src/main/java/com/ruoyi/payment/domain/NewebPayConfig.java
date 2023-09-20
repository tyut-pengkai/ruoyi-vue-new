package com.ruoyi.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewebPayConfig {
    /**
     * 藍新申請的商店代號
     */
    private String MerchantID;
    private String HashKey;
    private String HashIV;
    /**
     * 用來接收藍新付款通知的callback url
     */
    private String NotifyUrl;
    /**
     * 首次信用卡授權完成後要回到你系統的位置
     */
    private String ReturnUrl;
}
