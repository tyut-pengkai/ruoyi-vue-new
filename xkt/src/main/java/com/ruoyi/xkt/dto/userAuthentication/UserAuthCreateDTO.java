package com.ruoyi.xkt.dto.userAuthentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuthCreateDTO {

    @ApiModelProperty(value = "真实名称", required = true)
    private String realName;
    @ApiModelProperty(value = "联系电话", required = true)
    private String phonenumber;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCard;
    @ApiModelProperty(value = "人脸图片", required = true)
    private UACFileDTO faceFile;
    @ApiModelProperty(value = "国徽图片", required = true)
    private UACFileDTO emblemFile;

    @Data
    public static class UACFileDTO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
