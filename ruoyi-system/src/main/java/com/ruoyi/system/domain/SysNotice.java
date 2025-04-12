package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.XktBaseEntity;
import com.ruoyi.common.xss.Xss;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 通知公告表 sys_notice
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNotice extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 1 系统  2 档口
     */
    private Integer ownerType;
    /**
     * 生效开始时间 yyyy-MM-dd HH:mm
     */
    private Date effectStart;
    /**
     * 生效结束时间 yyyy-MM-dd HH:mm
     */
    private Date effectEnd;
    /**
     * 是否永久生效
     */
    private Integer perpetuity;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    private String remark;

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    public String getNoticeTitle() {
        return noticeTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("noticeId", getNoticeId())
                .append("noticeTitle", getNoticeTitle())
                .append("noticeType", getNoticeType())
                .append("noticeContent", getNoticeContent())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
