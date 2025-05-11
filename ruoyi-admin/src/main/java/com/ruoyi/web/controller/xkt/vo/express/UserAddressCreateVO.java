package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-05-11 23:19
 */
@ApiModel
@Data
public class UserAddressCreateVO {

    @NotEmpty
    @ApiModelProperty(value = "收件人名称")
    private String receiveName;

    @NotEmpty
    @ApiModelProperty(value = "收件人电话")
    private String receivePhone;

    @NotEmpty
    @ApiModelProperty(value = "收件人完整地址")
    private String address;
}
