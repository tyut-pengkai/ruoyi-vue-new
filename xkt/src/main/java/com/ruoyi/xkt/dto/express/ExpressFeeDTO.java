package com.ruoyi.xkt.dto.express;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressFeeDTO extends ExpressDTO{

    private BigDecimal expressFee;
}
