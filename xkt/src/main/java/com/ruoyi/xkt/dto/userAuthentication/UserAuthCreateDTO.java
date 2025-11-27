package com.ruoyi.xkt.dto.userAuthentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class UserAuthCreateDTO {

    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "联系电话")
    private String phonenumber;
    @ApiModelProperty(value = "验证码")
    private String code;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "人脸图片")
    private UACFileDTO faceFile;
    @ApiModelProperty(value = "国徽图片")
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
