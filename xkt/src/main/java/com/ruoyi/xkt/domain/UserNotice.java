package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户所有通知对象 user_notice
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotice extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户通知ID
     */
    @TableId
    private Long id;

    /**
     * sys_notice.id
     */
    private Long noticeId;

    /**
     * sys_user.id
     */
    private Long userId;

    /**
     * （0未读 1已读）
     */
    private Integer readStatus;

    /**
     * 凭证日期
     */
    private Date voucherDate;

}
