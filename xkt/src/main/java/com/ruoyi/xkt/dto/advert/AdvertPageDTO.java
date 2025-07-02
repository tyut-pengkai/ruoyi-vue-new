package com.ruoyi.xkt.dto.advert;

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
public class AdvertPageDTO extends BasePageDTO {

    @ApiModelProperty("上线状态")
    private Integer onlineStatus;
    @ApiModelProperty(value = "推广平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;

}
