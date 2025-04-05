package com.ruoyi.xkt.dto.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品主图返回数据")
@Data
public class StoreProdMainPicDTO {

    @ApiModelProperty("档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

}
