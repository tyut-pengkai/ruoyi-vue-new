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
public class StoreProdFileResVO {

    @ApiModelProperty("档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(name = "系统文件ID")
    private Long fileId;
    @ApiModelProperty(name = "文件路径")
    private String fileUrl;
    @ApiModelProperty(name = "文件类型")
    private String fileType;
    @ApiModelProperty(name = "文件名称")
    private String fileName;
    @ApiModelProperty(name = "文件大小")
    private BigDecimal fileSize;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
