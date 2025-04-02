package com.ruoyi.xkt.dto.express;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流费用配置
 *
 * @author liangyq
 * @date 2025-04-02 15:00
 */
@Data
public class ExpressFeeConfigDTO {
    /**
     * ID
     */
    private Long id;
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
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Long version;
}
