package com.ruoyi.xkt.dto.express;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-15 14:57
 */
@Data
public class ExpressRegionTreeNodeDTO implements Serializable {
    /**
     * ID
     */
    private Long id;
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
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 下级地区列表
     */
    private List<ExpressRegionTreeNodeDTO> children;

}
