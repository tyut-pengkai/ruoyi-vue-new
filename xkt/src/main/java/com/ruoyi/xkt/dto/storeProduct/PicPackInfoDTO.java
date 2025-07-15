package com.ruoyi.xkt.dto.storeProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicPackInfoDTO {
    /**
     * 系统文件ID
     */
    private Long fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 文件大小(M)
     */
    private BigDecimal fileSize;
    /**
     * 完整下载路径
     */
    private String downloadUrl;
    /**
     * 是否需要验证才能获取下载地址
     */
    private Boolean needVerify;

    public PicPackInfoDTO(Boolean needVerify) {
        this.needVerify = needVerify;
    }
}
