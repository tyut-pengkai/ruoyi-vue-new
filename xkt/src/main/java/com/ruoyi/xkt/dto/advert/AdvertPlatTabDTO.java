package com.ruoyi.xkt.dto.advert;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广营销平台与tab数据")
@Data
@Accessors(chain = true)
public class AdvertPlatTabDTO {

    private Integer platformId;
    private Integer tabId;
    private Integer typeId;
    private Long advertId;

}
