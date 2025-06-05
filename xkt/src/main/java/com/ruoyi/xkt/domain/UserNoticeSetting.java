package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户通知接收设置对象 user_notice_setting
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNoticeSetting extends XktBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户设置是否接收通知ID
     */
    @TableId
    private Long id;
    /**
     * sys_user.id
     */
    private Long userId;
    /**
     * 是否允许接收系统消息通知 0不接收 1接收
     */
    private Integer sysMsgNotice;
    /**
     * [电商卖家]是否允许接收代发订单消息通知 0不接收 1接收
     */
    private Integer orderNotice;
    /**
     * [电商卖家]是否允许接收关注档口消息通知 0不接收 1接收
     */
    private Integer focusNotice;
    /**
     * [电商卖家]是否允许接收收藏商品消息通知 0不接收 1接收
     */
    private Integer favoriteNotice;
    /**
     * [档口]是否允许接收广告消息通知 0不接收 1接收
     */
    private Integer advertNotice;
    /**
     * [档口]是否允许接收商品消息通知 0不接收 1接收
     */
    private Integer prodNotice;
    /**
     * [档口]是否允许接收短信通知 0不接收 1接收
     */
    private Integer smsNotice;
    /**
     * [档口]接收短信通知开始时间 yyyy-MM-dd HH:mm
     */
    private Date smsNoticeStart;
    /**
     * [档口]接收短信通知结束时间 yyyy-MM-dd HH:mm
     */
    private Date smsNoticeEnd;

}
