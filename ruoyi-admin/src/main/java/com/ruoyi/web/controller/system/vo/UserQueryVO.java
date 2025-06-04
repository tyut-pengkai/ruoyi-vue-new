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
public class UserQueryVO extends BasePageVO {
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private List<Long> userIds;
    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userName;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;
    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;
    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phonenumber;
    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;
    /**
     * 档口ID
     */
    @ApiModelProperty("档口ID")
    private List<Long> storeIds;
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private List<Long> roleIds;
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
