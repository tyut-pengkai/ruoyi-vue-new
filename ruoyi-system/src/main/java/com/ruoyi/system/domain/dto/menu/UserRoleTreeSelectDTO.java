package com.ruoyi.system.domain.dto.menu;

import com.ruoyi.common.core.domain.vo.menu.TreeSelectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户选中的菜单树
 *
 * @author ruoyi
 */
@Data
public class UserRoleTreeSelectDTO implements Serializable {

    @ApiModelProperty(value = "用户选中的菜单")
    List<Long> checkedIdList;
    @ApiModelProperty(value = "菜单树")
    List<TreeSelectDTO> tree;

}
