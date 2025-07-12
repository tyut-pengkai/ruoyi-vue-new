package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-05 15:41
 */
@ApiModel
@Data
public class AvatarChangeVO {

    @NotEmpty(message = "头像不能为空")
    @ApiModelProperty("头像（图片上传到公有桶：avatar目录下）")
    private String avatar;

}
