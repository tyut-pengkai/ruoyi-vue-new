package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-06-11
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressFeeConfigListItemVO extends ExpressFeeConfigVO {

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "物流名称")
    private String expressName;
}
