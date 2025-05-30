package com.ruoyi.xkt.dto.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("筛选档口最新的4个商品")
@Data
@RequiredArgsConstructor
public class StoreProdFileLatestFourProdDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "主图url")
    private String mainPicUrl;

}
