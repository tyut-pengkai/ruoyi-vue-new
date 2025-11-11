package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档口当前商品颜色对象 store_product_color
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductColor extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品颜色ID
     */
    @TableId
    private Long id;
    /**
     * 档口颜色ID
     */
    private Long storeColorId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 颜色名称
     */
    private String colorName;
    /**
     * 内里材质
     */
    private String shoeUpperLiningMaterial;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 商品状态 2：在售 3：尾货 4：已下架
     */
    private Integer prodStatus;

}
