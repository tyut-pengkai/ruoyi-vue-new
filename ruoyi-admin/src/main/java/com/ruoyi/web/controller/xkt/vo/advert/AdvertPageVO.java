package com.ruoyi.web.controller.xkt.vo.advert;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
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
public class AdvertPageVO extends BasePageVO {

    @ApiModelProperty(value = "上线状态")
    private Integer onlineStatus;
    @ApiModelProperty(value = "推广平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;

}
