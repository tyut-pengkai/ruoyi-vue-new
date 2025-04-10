package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
public class ShoppingCartDetail extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户进货车ID
     */
    @TableId
    private Long id;
    /**
     * shoppingCart.id
     */
    @Excel(name = "shoppingCart.id")
    private Long shoppingCartId;
    /**
     * store_prod_color.id
     */
    @Excel(name = "store_prod_color.id")
    private Long storeProdColorId;

    /**
     * 尺码
     */
    private Integer size;

    /**
     * 档口颜色ID
     */
    private Long storeColorId;
    /**
     * 颜色名称
     */
    @Excel(name = "颜色名称")
    private String colorName;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Integer quantity;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdColorId", getStoreProdColorId())
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
