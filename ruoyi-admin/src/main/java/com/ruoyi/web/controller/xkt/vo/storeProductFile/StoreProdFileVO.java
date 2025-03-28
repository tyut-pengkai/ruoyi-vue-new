package com.ruoyi.web.controller.xkt.vo.storeProductFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品文件")
@Data
public class StoreProdFileVO {

    @NotBlank(message = "文件名称不能为空!")
    @ApiModelProperty(name = "文件名称")
    private String fileName;
    @NotBlank(message = "文件路径不能为空!")
    @ApiModelProperty(name = "文件路径")
    private String fileUrl;
    @NotNull(message = "文件大小不能为空!")
    @ApiModelProperty(name = "文件大小")
    private BigDecimal fileSize;
    @NotBlank(message = "文件类型不能为空!")
    @ApiModelProperty(name = "文件类型")
    private String fileType;
    @ApiModelProperty(name = "排序")
    @NotNull(message = "排序不能为空!")
    private Integer orderNum;

}
