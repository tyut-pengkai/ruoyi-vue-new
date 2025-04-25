package com.ruoyi.framework.img.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-25 20:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgSearchResult {
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 图片名称
     */
    private String picName;
    /**
     * 图片相似打分。取值范围：0~1
     */
    private Float score;
    /**
     * 图片类目
     */
    private Integer categoryId;
    /**
     * 用户自定义的内容
     */
    private String customContent;
}
