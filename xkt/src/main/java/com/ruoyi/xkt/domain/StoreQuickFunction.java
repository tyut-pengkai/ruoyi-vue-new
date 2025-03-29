package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口快捷功能对象 store_quick_function
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreQuickFunction extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口快捷功能ID
     */
    @TableId
    private Long id;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * 快捷功能名称
     */
    @Excel(name = "快捷功能名称")
    private String funcName;

    /**
     * 快捷功能图标
     */
    @Excel(name = "快捷功能图标")
    private String funcIcon;

    /**
     * 快捷功能路径
     */
    @Excel(name = "快捷功能路径")
    private String funcUrl;

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
                .append("funcName", getFuncName())
                .append("funcIcon", getFuncIcon())
                .append("funcUrl", getFuncUrl())
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
