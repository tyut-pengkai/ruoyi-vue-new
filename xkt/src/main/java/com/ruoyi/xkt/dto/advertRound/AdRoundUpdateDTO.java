package com.ruoyi.xkt.dto.advertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口上传推广图 或 修改商品")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundUpdateDTO {


    @NotNull(message = "推广轮次ID不能为空!")
    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图")
    private ARUFileDTO file;
    @ApiModelProperty(value = "商品ID字符串")
    private String prodIdStr;

    @Data
    public static class ARUFileDTO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
