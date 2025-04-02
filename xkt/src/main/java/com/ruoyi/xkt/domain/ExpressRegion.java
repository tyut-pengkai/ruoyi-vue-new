package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 物流行政区划
 *
 * @author liangyq
 * @date 2025-04-02 14:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressRegion extends SimpleEntity {
    /**
     * 地区编码，基于行政区划代码做扩展，唯一约束
     */
    private String regionCode;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 地区级别[1:省 2:市 3:区县]
     */
    private Integer regionLevel;
    /**
     * 上级地区编码，没有上级的默认空
     */
    private String parentRegionCode;
    /**
     * 上级地区名称，冗余
     */
    private String parentRegionName;
    /**
     * 版本号
     */
    @Version
    private Long version;
}
