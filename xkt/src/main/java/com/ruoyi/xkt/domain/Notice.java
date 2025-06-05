package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 通知公告表 sys_notice
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Notice extends XktBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 公告ID
     */
    private Long id;
    /**
     * 公告标题
     */
    private String noticeTitle;
    /**
     * 公告类型（1通知 2公告）
     */
    private Integer noticeType;
    /**
     * 公告内容
     */
    private String noticeContent;
    /**
     * 1 系统  2 档口
     */
    private Integer ownerType;
    /**
     * 档口id
     */
    private Long storeId;
    /**
     * 生成公告userId
     */
    private Long userId;
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

}
