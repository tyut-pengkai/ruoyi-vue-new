package com.ruoyi.system.domain.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuDTO {

    private String menuName;
    private Integer orderNum;
    private String path;
    private String component;
    private String icon;

}
