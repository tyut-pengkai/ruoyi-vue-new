package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口子角色对象 store_role
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreRole extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口角色ID
     */
    @TableId
    private Long id;

    /**
     * store.id
     */
    private Long storeId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统角色ID
     */
    private Long roleId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人名称
     */
    private String operatorName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
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
