package com.ruoyi.common.core.domain.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
@Data
public class TreeSelectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "节点ID")
    private Long id;
    @ApiModelProperty(value = "节点名称")
    private String label;
    @ApiModelProperty(value = "节点禁用")
    private boolean disabled = false;
    @ApiModelProperty(value = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVO> children;

}
