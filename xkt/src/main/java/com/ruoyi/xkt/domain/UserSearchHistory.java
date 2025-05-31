package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户搜索历史 user_search_history
 *
 * @author ruoyi
 * @date 2025-05-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserSearchHistory extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索历史ID
     */
    @TableId
    private Long id;
    /**
     * 搜索用户ID
     */
    private Long userId;
    /**
     * 搜索用户名称
     */
    private String userName;
    /**
     * 搜索内容
     */
    private String searchContent;
    /**
     * 搜索平台
     */
    private Integer platformId;
    /**
     * 统计时间
     */
    private Date voucherDate;

}
