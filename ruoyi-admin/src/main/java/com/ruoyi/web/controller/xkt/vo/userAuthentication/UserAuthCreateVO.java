package com.ruoyi.web.controller.xkt.vo.userAuthentication;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuthCreateVO {

    @NotBlank(message = "真实名称不能为空")
    @ApiModelProperty(value = "真实名称", required = true)
    private String realName;
    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话", required = true)
    private String phonenumber;
    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCard;
    @NotNull(message = "人脸图片不能为空")
    @ApiModelProperty(value = "人脸图片", required = true)
    private UACFileDTO faceFile;
    @NotNull(message = "国徽图片不能为空")
    @ApiModelProperty(value = "国徽图片", required = true)
    private UACFileDTO emblemFile;

    @Data
    @ApiModel
    public static class UACFileDTO {
        @NotBlank(message = "文件名称不能为空")
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @NotBlank(message = "文件路径不能为空")
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @NotNull(message = "文件大小不能为空")
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
    }

}
