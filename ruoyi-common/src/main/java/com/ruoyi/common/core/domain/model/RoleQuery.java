package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 19:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleQuery {
    /**
     * 角色ID
     */
    private List<Long> roleIds;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 档口ID
     */
    private List<Long> storeIds;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间-开始
     */
    private Date createTimeBegin;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

}
