package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-03-26 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicZipDTO {
    /**
     * 原始图包key
     */
    private String key;
    /**
     * 450图包key
     */
    private String keyOf450;
    /**
     * 750图包key
     */
    private String keyOf750;
}
