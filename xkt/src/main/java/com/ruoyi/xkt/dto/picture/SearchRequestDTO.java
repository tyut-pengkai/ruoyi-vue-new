package com.ruoyi.xkt.dto.picture;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-21
 */
@Data
@Accessors(chain = true)
public class SearchRequestDTO {
    /**
     * 用于搜索的图片对应ossKey
     */
    private String picKey;
    /**
     * 图搜的图片名称
     */
    private String picName;
    /**
     * 用于搜索的图片大小，不超过4M
     */
    private BigDecimal picSize;
    /**
     * 期望返回条数
     */
    private Integer num;
}
