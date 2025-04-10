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
public class ShoppingCart extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户进货车ID
     */
    @TableId
    private Long id;

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
     * store_prod.id
     */
    @Excel(name = "store_prod.id")
    private Long storeProdId;
    /**
     * 商品货号
     */
    @Excel(name = "商品货号")
    private String prodArtNum;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("storeId", getStoreId())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
