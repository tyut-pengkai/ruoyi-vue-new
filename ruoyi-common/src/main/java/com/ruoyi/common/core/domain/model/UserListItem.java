package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 19:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserListItem extends SysUser {
    /**
     * 角色集
     */
    private List<Role> roles;

    @Data
    public static class Role {
        /**
         * 角色ID
         */
        private Long roleId;
        /**
         * 角色名称
         */
        private String roleName;
        /**
         * 档口ID
         */
        private Long storeId;
        /**
         * 档口名称
         */
        private String storeName;
    }
}
