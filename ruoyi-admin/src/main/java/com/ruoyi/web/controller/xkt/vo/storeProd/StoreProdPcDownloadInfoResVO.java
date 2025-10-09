package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreProdPcDownloadInfoResVO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "档口下载图包列表")
    List<SPPDIFileVO> fileList;

    @Data
    public static class SPPDIFileVO {
        @ApiModelProperty(value = "文件id")
        private Long fileId;
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型 3下载图片包 4 450px 5 750px")
        private Integer fileType;
    }

}
