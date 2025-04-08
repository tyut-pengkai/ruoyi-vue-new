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
 * 档口首页对象 store_homepage
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreHomepage extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口首页ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 档口各位置类型
     */
    @Excel(name = "档口各位置类型")
    private Integer type;

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


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
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
