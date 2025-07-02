package com.ruoyi.xkt.dto.storeProdDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdDetailDTO {

    @ApiModelProperty(value = "详情内容")
    private String detail;

}
