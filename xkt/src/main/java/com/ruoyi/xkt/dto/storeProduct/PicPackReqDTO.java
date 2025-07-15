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
     * 图片验证码UUID
     */
    private String uuid;
    /**
     * 图片验证码CODE
     */
    private String code;
}
