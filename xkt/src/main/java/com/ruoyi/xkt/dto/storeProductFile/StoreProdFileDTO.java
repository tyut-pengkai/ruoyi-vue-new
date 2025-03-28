package com.ruoyi.xkt.dto.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品文件")
@Data
public class StoreProdFileDTO {

    @ApiModelProperty(name = "系统文件ID")
    private Long fileId;
    @ApiModelProperty(name = "文件类型")
    private String fileType;
    @ApiModelProperty(name = "文件大小")
    private BigDecimal fileSize;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
