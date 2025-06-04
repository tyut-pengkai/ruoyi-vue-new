package com.ruoyi.common.core.domain.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 19:36
 */
@Data
public class UserQuery {
    /**
     * 用户ID
     */
    private List<Long> userIds;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 档口ID
     */
    private List<Long> storeIds;
    /**
     * 角色ID
     */
    private List<Long> roleIds;
    /**
     * 创建时间-开始
     */
    private Date createTimeBegin;
    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

}
