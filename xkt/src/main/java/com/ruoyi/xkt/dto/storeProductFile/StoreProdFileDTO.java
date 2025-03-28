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

    @ApiModelProperty(name = "文件名称")
    private String fileName;
    @ApiModelProperty(name = "文件路径")
    private String fileUrl;
    @ApiModelProperty(name = "文件大小")
    private BigDecimal fileSize;
    @ApiModelProperty(name = "文件类型")
    private String fileType;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
