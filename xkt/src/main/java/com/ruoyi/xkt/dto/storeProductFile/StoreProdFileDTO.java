package com.ruoyi.xkt.dto.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品文件")
@Data
@RequiredArgsConstructor
public class StoreProdFileDTO {

    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;
    @ApiModelProperty(value = "文件大小")
    private BigDecimal fileSize;
    @ApiModelProperty(value = "文件类型")
    private Integer fileType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

}
