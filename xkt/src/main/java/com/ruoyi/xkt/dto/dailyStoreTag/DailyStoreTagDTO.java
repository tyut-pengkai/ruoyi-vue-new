package com.ruoyi.xkt.dto.dailyStoreTag;

import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/4/16 23:06
 */
@Data
public class DailyStoreTagDTO {

    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 销售数量
     */
    private Integer count;

}
