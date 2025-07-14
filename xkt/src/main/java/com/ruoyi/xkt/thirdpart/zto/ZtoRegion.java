package com.ruoyi.xkt.thirdpart.zto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-14
 */
@Data
public class ZtoRegion {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 上级主键
     */
    private Long parentId;
    /**
     * 编码
     */
    private String code;
    /**
     * 国家标准编码
     */
    private String nationalCode;
    /**
     * 全称
     */
    private String fullName;
    /**
     * 层级
     */
    private Integer layer;
    /**
     * 是否有效
     */
    private Integer enabled;
}
