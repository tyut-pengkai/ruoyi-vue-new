package com.ruoyi.xkt.dto.storeProduct;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-07-15
 */
@Data
public class PicPackSimpleDTO {
    /**
     * 文件ID
     */
    private Long fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小(M)
     */
    private BigDecimal fileSize;
    /**
     * 文件类型（1主图、2视频、3下载）
     */
    private Integer fileType;
}
