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
public class PicPackInfoVO {
    /**
     * 系统文件ID
     */
    @ApiModelProperty(value = "系统文件ID")
    private Long fileId;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;
    /**
     * 文件大小(M)
     */
    @ApiModelProperty(value = "文件大小(M)")
    private BigDecimal fileSize;
    /**
     * 完整下载路径
     */
    @ApiModelProperty(value = "完整下载路径")
    private String downloadUrl;
    /**
     * 是否需要验证才能获取下载地址
     */
    @ApiModelProperty(value = "是否需要验证才能获取下载地址")
    private Boolean needVerify;

}
