package com.ruoyi.web.controller.xkt.vo.storeFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口工厂")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreFactoryVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口工厂ID[新增不传 编辑必传]")
    private Long storeFactoryId;
    @NotBlank(message = "工厂名称不能为空!")
    @ApiModelProperty(value = "工厂名称", required = true)
    private String facName;
    @ApiModelProperty(value = "工厂地址")
    private String facAddress;
    @ApiModelProperty(value = "工厂联系电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确，请输入有效的中国大陆手机号")
    private String facPhone;
    @ApiModelProperty(value = "备注")
    private String remark;

}
