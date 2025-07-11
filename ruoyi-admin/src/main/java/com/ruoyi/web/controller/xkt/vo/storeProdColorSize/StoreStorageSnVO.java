package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel(value = "入库条码查询model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreStorageSnVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private String storeId;
    @NotNull(message = "条码列表不能为空!")
    @ApiModelProperty(value = "条码列表", required = true)
    private List<String> snList;

}
