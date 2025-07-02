package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminAdRoundCancelInterceptVO {

    @NotNull(message = "推广轮次ID不能为空")
    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
