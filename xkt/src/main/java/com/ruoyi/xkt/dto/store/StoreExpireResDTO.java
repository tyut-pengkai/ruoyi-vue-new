package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreExpireResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "目标 1[正式版] 2[实力质造会员]")
    private Integer target;
    @ApiModelProperty(value = "试用过期时间  正式版过期时间  会员过期时间")
    private Date serviceEndTime;

}
