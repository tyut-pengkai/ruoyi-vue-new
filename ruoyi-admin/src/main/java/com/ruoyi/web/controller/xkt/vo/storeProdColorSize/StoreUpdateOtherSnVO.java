package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreUpdateOtherSnVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @Valid
    @NotNull(message = "更新条码列表不能为空!")
    @ApiModelProperty(value = "更新条码列表", required = true)
    private List<SUOSnVO> snList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SUOSnVO {
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @NotBlank(message = "商品货号不能为空!")
        @ApiModelProperty(value = "商品货号", required = true)
        private String prodArtNum;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotBlank(message = "录入的条码")
        @ApiModelProperty(value = "条码", required = true)
        @Size(min = 0, max = 50, message = "条码长度不能超过100个字符")
        private String sn;
    }


}
