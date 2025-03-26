package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口首页对象 store_homepage
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreHomepage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口首页ID
     */
    private Long storeHomeId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 档口各位置类型
     */
    @Excel(name = "档口各位置类型")
    private Long type;

    /**
     * 档口各位置文件ID
     */
    @Excel(name = "档口各位置文件ID")
    private Long fileId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer orderNum;

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
                .append("storeHomeId", getStoreHomeId())
                .append("storeId", getStoreId())
                .append("type", getType())
                .append("fileId", getFileId())
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
