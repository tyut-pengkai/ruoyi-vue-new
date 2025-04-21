package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档口商品统计数据 store_product_statistics
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductStatistics extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口商品ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    private Long storeId;

    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 商品浏览量
     */
    private Long viewCount;
    /**
     * 商品下载量
     */
    private Long downloadCount;

}
