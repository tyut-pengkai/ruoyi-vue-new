package com.ruoyi.xkt.dto.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统拦截档口推广位")
@Data
@Accessors(chain = true)

public class AdminAdRoundSysInterceptDTO {

    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @ApiModelProperty(value = "投放状态")
    private Integer launchStatus;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "管理员上传推广图")
    private AARSIFileVO file;
    @ApiModelProperty(value = "档口商品ID集合")
    private List<Long> storeProdIdList;
    @ApiModelProperty(value = "风格类型")
    private Integer styleType;

    @Data
    @ApiModel(value = "推广图对象")
    public static class AARSIFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
