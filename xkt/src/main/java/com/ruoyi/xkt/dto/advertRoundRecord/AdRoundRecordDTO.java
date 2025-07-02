package com.ruoyi.xkt.dto.advertRoundRecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Builder
@Accessors(chain = true)
public class AdRoundRecordDTO {

    @ApiModelProperty(value = "广告轮次ID")
    private Integer roundId;
    @ApiModelProperty(value = "竞价状态")
    private Integer biddingStatus;

}
