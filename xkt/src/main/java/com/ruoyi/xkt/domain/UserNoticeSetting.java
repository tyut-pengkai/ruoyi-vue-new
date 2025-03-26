package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户通知接收设置对象 user_notice_setting
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNoticeSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户设置是否接收通知ID
     */
    private Long userNoticeSetId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * 通知类型
     */
    @Excel(name = "通知类型")
    private String noticeType;

    /**
     * 是否允许通知（0禁止 1允许）
     */
    @Excel(name = "是否允许通知", readConverterExp = "0=禁止,1=允许")
    private String allow;

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
                .append("userNoticeSetId", getUserNoticeSetId())
                .append("userId", getUserId())
                .append("noticeType", getNoticeType())
                .append("allow", getAllow())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
