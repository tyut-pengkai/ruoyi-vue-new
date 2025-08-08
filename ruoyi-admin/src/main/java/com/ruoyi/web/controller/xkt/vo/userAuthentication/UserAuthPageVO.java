package com.ruoyi.web.controller.xkt.vo.userAuthentication;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UserAuthPageVO extends BasePageVO {

    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "停用状态 0正常 2停用")
    private String delFlag;
    @ApiModelProperty(value = "代发状态")
    private Integer authStatus;

}
