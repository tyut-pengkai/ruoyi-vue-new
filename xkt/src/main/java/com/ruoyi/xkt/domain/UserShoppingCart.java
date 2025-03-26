package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户进货车对象 user_shopping_cart
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserShoppingCart extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户进货车ID
     */
    private Long userShopCartId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * store_prod_color_size.id
     */
    @Excel(name = "store_prod_color_size.id")
    private Long storeProdColorSizeId;

    /**
     * store_prod.id
     */
    @Excel(name = "store_prod.id")
    private Long storeProdId;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Integer quantity;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userShopCartId", getUserShopCartId())
                .append("userId", getUserId())
                .append("storeId", getStoreId())
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("storeProdId", getStoreProdId())
                .append("quantity", getQuantity())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
