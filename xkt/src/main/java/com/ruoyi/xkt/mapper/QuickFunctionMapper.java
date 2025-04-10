package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.QuickFunction;
import org.apache.ibatis.annotations.Param;

/**
 * 档口快捷功能Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface QuickFunctionMapper extends BaseMapper<QuickFunction> {

    /**
     * 将指定角色指定bizId的快捷功能置为无效
     *
     * @param roleId 角色ID
     * @param bizId  业务ID
     */
    void updateDelFlagByRoleIdAndBizId(@Param("roleId") Long roleId, @Param("bizId") Long bizId);

}
