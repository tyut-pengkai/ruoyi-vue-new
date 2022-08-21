package com.ruoyi.api.v1.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppUserVo extends SysAppUser {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String createBy;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @JsonIgnore
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String updateBy;

    /**
     * 更新时间
     */
    @JSONField(serialize = false)
    @JsonIgnore
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 搜索值
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String searchValue;

    /**
     * 请求参数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, Object> params;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String delFlag;

    /**
     * 软件用户ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long appUserId;

    /**
     * 用户ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long userId;

    /**
     * 软件ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long appId;

    /**
     * 密码连续错误次数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Integer pwdErrorTimes;

    /**
     * 单码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String loginCode;

    /**
     * 所属账号信息
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysUser user;

    /**
     * 所属账号信息
     */
    private SysUserVo userInfo;

    /**
     * 所属软件信息
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysApp app;

    /**
     * 所属用户用户名称，用户承载搜索参数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String userName;

    @JSONField(serialize = false)
    @JsonIgnore
    private Integer effectiveLoginLimitU;

    @JSONField(serialize = false)
    @JsonIgnore
    private Integer effectiveLoginLimitM;

    @JSONField(serialize = false)
    @JsonIgnore
    private Integer currentOnlineU;

    @JSONField(serialize = false)
    @JsonIgnore
    private Integer currentOnlineM;

    public SysAppUserVo(SysAppUser v) {
        BeanUtils.copyProperties(v, this);
        if (v.getUserId() != null) {
            this.userInfo = new SysUserVo(v.getUser());
        }
    }
}
