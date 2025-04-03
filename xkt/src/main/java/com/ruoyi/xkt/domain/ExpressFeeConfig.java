package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 物流费用配置
 *
 * @author liangyq
 * @date 2025-04-02 14:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressFeeConfig extends SimpleEntity {
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 地区编码，基于行政区划代码做扩展，唯一约束
     */
    private String regionCode;
    /**
     * 上级地区编码，没有上级的默认空
     */
    private String parentRegionCode;
    /**
     * 首件运费
     */
    private BigDecimal firstItemAmount;
    /**
     * 续费
     */
    private BigDecimal nextItemAmount;
    /**
     * 版本号
     */
    @Version
    private Long version;
}
