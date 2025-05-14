package com.ruoyi.web.controller.xkt.vo.advertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口上传推广图")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundUploadPicVO {

    @NotNull(message = "推广轮次ID不能为空!")
    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @NotBlank(message = "文件名称不能为空!")
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @NotBlank(message = "文件路径不能为空!")
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;
    @NotNull(message = "文件大小不能为空!")
    @ApiModelProperty(value = "文件大小")
    private BigDecimal fileSize;

}
