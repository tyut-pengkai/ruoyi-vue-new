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
 * 档口所有颜色对象 store_color
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreColor extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品颜色ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 颜色名称
     */
    @Excel(name = "颜色名称")
    private String colorName;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer orderNum;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("colorName", getColorName())
                .append("orderNum", getOrderNum())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
