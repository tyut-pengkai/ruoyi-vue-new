package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserInfoEdit extends SysUser {
    /**
     * 角色ID集
     */
    private List<Long> roleIds;
}
