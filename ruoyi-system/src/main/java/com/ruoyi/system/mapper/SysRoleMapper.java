package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.RoleInfo;
import com.ruoyi.common.core.domain.model.RoleListItem;
import com.ruoyi.common.core.domain.model.RoleQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 查询角色信息
     *
     * @param roleId
     * @return
     */
    RoleInfo getRoleInfoById(@Param("roleId") Long roleId);

    /**
     * 查询角色列表
     *
     * @param query
     * @return
     */
    List<RoleListItem> listRole(RoleQuery query);

}
