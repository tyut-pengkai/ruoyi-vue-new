package com.ruoyi.xkt.dto.storeProduct;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-15
 */
@Data
public class PicPackReqDTO {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 文件ID
     */
    private Long fileId;
    /**
     * ticket（图像验证参数）
     */
    private String ticket;
    /**
     * randstr（图像验证参数）
     */
    private String randstr;
}
