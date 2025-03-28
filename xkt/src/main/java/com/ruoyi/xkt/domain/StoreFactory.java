package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口合作工厂对象 store_factory
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreFactory extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口工厂ID
     */
    @TableId
    private Long storeFacId;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * 工厂名称
     */
    @Excel(name = "工厂名称")
    private String facName;

    /**
     * 工厂地址
     */
    @Excel(name = "工厂地址")
    private String facAddress;

    /**
     * 工厂联系电话
     */
    @Excel(name = "工厂联系电话")
    private String facPhone;

    /**
     * 工厂状态
     */
    @Excel(name = "工厂状态")
    private String facStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeFacId", getStoreFacId())
                .append("storeId", getStoreId())
                .append("facName", getFacName())
                .append("facAddress", getFacAddress())
                .append("facPhone", getFacPhone())
                .append("facStatus", getFacStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
