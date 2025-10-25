package com.ruoyi.web.controller.xkt.migartion.vo.gtOnly;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class GtOnlyInitVO {

    private Integer userId;
    private Long storeId;
    // 大小码加价金额 0 or other
    private BigDecimal addOverPrice;

}
