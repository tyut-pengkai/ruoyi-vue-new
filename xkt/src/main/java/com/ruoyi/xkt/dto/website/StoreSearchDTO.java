package com.ruoyi.xkt.dto.website;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("档口排行榜搜索")
@Data
public class StoreSearchDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;

}
