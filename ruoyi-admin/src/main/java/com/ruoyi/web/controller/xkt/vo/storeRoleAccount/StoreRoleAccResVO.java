package com.ruoyi.web.controller.xkt.vo.storeRoleAccount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreRoleAccResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口子账号ID")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "子账户名称")
    private String accountName;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "档口子角色ID")
    private String storeRoleId;
    @ApiModelProperty(value = "档口子角色名称")
    private String storeRoleName;
    @ApiModelProperty(value = "启用0 停用2")
    private String delFlag;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "创建人名称")
    private String operatorName;

}
