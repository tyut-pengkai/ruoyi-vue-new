package com.ruoyi.xkt.dto.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品文件返回数据")
@Data
public class StoreProdFilePicSpaceResDTO {

    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "文件路径")
    private String fileUrl;

}
