package com.ruoyi.common.core.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 数据权限模型
 * @Author DuanYangYang
 * @Date 2024/5/23/023 10:25
 **/
@Data
@Builder
public class DataPermissionModel implements Serializable {

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    /**
     * 类型（见 DataPermissionType）
     */
    private String type;

    /**
     * 当DataPermissionType 为 DEPT 时有值
     */
    private List<Long> deptIds;

    /**
     * 当DataPermissionType 为 SELF 时有值
     */
    private Long userId;

    /**
     * 要进行数据权限过滤的mapper类名列表
     */
    private List<String> mappers;


}
