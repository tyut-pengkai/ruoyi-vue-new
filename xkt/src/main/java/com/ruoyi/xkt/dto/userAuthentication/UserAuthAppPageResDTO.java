package com.ruoyi.xkt.dto.userAuthentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class UserAuthAppPageResDTO {

    @ApiModelProperty(value = "代发ID")
    private Long userAuthId;
    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "联系电话")
    private String phonenumber;
    @ApiModelProperty(value = "头像")
    private String avatar;

}
