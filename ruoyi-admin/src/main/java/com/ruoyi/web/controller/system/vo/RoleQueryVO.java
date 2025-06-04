package com.ruoyi.web.controller.system.vo;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 19:36
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleQueryVO extends BasePageVO {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private List<Long> roleIds;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @ApiModelProperty("角色权限")
    private String roleKey;

    /**
     * 档口ID
     */
    @ApiModelProperty("档口ID")
    private List<Long> storeIds;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty("角色状态（0正常 1停用）")
    private String status;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty("创建时间-开始")
    private Date createTimeBegin;

    /**
     * 创建时间-结束
     */
    @ApiModelProperty("创建时间-结束")
    private Date createTimeEnd;

}
