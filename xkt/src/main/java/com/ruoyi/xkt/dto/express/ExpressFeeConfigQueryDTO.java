package com.ruoyi.xkt.dto.express;

import lombok.Data;

/**
 * 物流费用配置
 *
 * @author liangyq
 * @date 2025-04-02 15:00
 */
@Data
public class ExpressFeeConfigQueryDTO {
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 地区编码，基于行政区划代码做扩展，唯一约束
     */
    private String regionCode;
    /**
     * 地区名称
     */
    private String regionName;
}
