package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)

public class AdminAdRoundSysInterceptVO {

    @NotNull(message = "推广轮次ID不能为空")
    @ApiModelProperty(value = "推广轮次ID", required = true)
    private Long advertRoundId;
    @NotBlank(message = "对象锁符号不能为空")
    @ApiModelProperty(value = "对象锁符号", required = true)
    private String symbol;
    @NotNull(message = "投放状态不能为空")
    @ApiModelProperty(value = "投放状态", required = true)
    private Integer launchStatus;
    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotBlank(message = "档口名称不能为空")
    @ApiModelProperty(value = "档口名称", required = true)
    private String storeName;
    @ApiModelProperty(value = "管理员上传推广图")
    private AARSIFileVO file;
    @ApiModelProperty(value = "档口商品ID集合")
    private List<Long> storeProdIdList;
    @ApiModelProperty(value = "风格类型")
    private Integer styleType;

    @Data
    public static class AARSIFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
