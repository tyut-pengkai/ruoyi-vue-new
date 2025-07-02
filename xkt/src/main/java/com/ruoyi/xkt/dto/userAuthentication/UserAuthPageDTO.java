package com.ruoyi.xkt.dto.userAuthentication;

import com.ruoyi.xkt.dto.BasePageDTO;
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
public class UserAuthPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "代发状态")
    private Integer authStatus;

}
