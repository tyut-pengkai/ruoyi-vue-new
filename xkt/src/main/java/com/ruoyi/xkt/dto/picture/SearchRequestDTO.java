package com.ruoyi.xkt.dto.picture;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-21
 */
@Data
public class SearchRequestDTO {
    /**
     * 用于搜索的图片对应ossKey
     */
    private String picKey;
    /**
     * 用于搜索的图片大小，不超过4M
     */
    private BigDecimal picSize;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 期望返回条数
     */
    private Integer num;
}
