package com.ruoyi.xkt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 用户所有通知对象 user_notice
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户通知ID
     */
    private Long userNoticeId;

    /**
     * sys_notice.id
     */
    @Excel(name = "sys_notice.id")
    private Integer noticeId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * （0未读 1已读）
     */
    @Excel(name = "", readConverterExp = "0=未读,1=已读")
    private String read;

    /**
     * 凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

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
                .append("userNoticeId", getUserNoticeId())
                .append("noticeId", getNoticeId())
                .append("userId", getUserId())
                .append("read", getRead())
                .append("voucherDate", getVoucherDate())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
