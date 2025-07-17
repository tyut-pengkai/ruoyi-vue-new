package com.ruoyi.xkt.dto.userSubscriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSubscPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "查询来源", notes = "1 PC, 2 APP")
    private Integer source;

}
