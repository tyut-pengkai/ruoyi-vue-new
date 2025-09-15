package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-07-15
 */
@ApiModel
@Data
public class PicPackSimpleVO {
    /**
     * 文件ID
     */
    @ApiModelProperty(value = "文件ID")
    private Long fileId;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /**
     * 文件大小(M)
     */
    @ApiModelProperty(value = "文件大小(M)")
    private BigDecimal fileSize;
    /**
     * 文件类型（1主图、2视频、3下载）
     */
    @ApiModelProperty(value = "文件类型[3:商品下载图片包 4:商品下载图片包450px 5:商品下载图片包750px]")
    private Integer fileType;
}
