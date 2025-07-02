package com.ruoyi.xkt.dto.userAuthentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class UserAuthPageResDTO {

    @ApiModelProperty(value = "代发ID")
    private Long userAuthId;
    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "联系电话")
    private String phonenumber;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "审核状态")
    private Integer authStatus;
    @ApiModelProperty(value = "审核状态名称")
    private String authStatusName;
    @ApiModelProperty(value = "启用状态")
    private Integer delFlag;

}
