package com.ruoyi.xkt.dto.picture;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-21
 */
@Data
public class SearchRequestDTO {

    private String picKey;

    private BigDecimal picSize;

    private Long userId;

    private Integer num;
}
