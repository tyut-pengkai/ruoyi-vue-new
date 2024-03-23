package com.ruoyi.common.utils.tree.vo;

import java.util.List;

/**
 * 无论是公司的分组，还是医院的科室等实体类，必须有自身ID和父节点ID
 */
public interface IBaseTree {
    Long getId();

    Long getParentId();

    List getChildren();
}
