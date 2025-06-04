package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTreeNode extends SysMenu {
    /**
     * 子节点
     */
    private List<MenuTreeNode> children;
}
