package com.ruoyi.web.controller.xkt.vo.express;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 物流费用配置
 *
 * @author liangyq
 * @date 2025-04-02 15:00
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressFeeConfigQueryVO extends BasePageVO {
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 地区编码
     */
    @ApiModelProperty(value = "地区编码")
    private String regionCode;
    /**
     * 地区名称
     */
    @ApiModelProperty(value = "地区名称")
    private String regionName;
}
