package com.ruoyi.api.v1.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserVo extends SysUser {
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
     * 用户ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long userId;

    /**
     * 部门ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long deptId;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String password;

    /**
     * 盐加密
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String delFlag;

    /**
     * 部门对象
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysDept dept;

    /**
     * 免费余额
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private BigDecimal availableFreeBalance;

    /**
     * 免费余额
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private BigDecimal freezeFreeBalance;

    /**
     * 支付余额
     */
    private BigDecimal availablePayBalance;

    /**
     * 支付余额
     */
    private BigDecimal freezePayBalance;

    /**
     * 免费消费
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private BigDecimal freePayment;

    /**
     * 支付消费
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private BigDecimal payPayment;

    /**
     * 角色对象
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long[] roleIds;

    /**
     * 岗位组
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long[] postIds;

    /**
     * 角色ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long roleId;

    public SysUserVo(SysUser v) {
        BeanUtils.copyProperties(v, this);
    }

}
